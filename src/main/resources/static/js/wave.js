(function () {
    const WAVE_LAYERS = [
        { color: "#BACCEB", opacity: 0.4,  amplitude: 6, frequency: 1.3, speed: 0.3, yOffset: 100 },
        { color: "#8AA4D0", opacity: 0.35, amplitude: 8, frequency: 0.9, speed: 0.4, yOffset: 80  },
        { color: "#5E7EB4", opacity: 0.35, amplitude: 9, frequency: 1.1, speed: 0.6, yOffset: 60  },
        { color: "#3A5C96", opacity: 0.35, amplitude: 7, frequency: 1.4, speed: 0.8, yOffset: 45  },
        { color: "#1E3D72", opacity: 0.35, amplitude: 5, frequency: 1.8, speed: 1.0, yOffset: 30  },
    ];

    const MOBILE_WIDTH = 430;   // 콘텐츠 기준 너비 (고정)
    const SVG_HEIGHT   = 300;
    const FILL_HEIGHT  = 260;

    // 파도 SVG는 뷰포트 전체 너비를 사용
    let WAVE_WIDTH = window.innerWidth;

    const paths = [
        document.getElementById("wave-0"),
        document.getElementById("wave-1"),
        document.getElementById("wave-2"),
        document.getElementById("wave-3"),
        document.getElementById("wave-4"),
    ];

    // SVG 요소 자체를 전체 너비로 확장
    // wave path들의 부모 SVG를 찾아서 너비 조정
    const svg = paths.find(Boolean)?.closest("svg");
    if (svg) {
        svg.style.position  = "absolute";
        svg.style.left      = "50%";
        svg.style.transform = "translateX(-50%)";
        svg.style.width     = "100vw";
        svg.style.overflow  = "visible";
    }

    WAVE_LAYERS.forEach((layer, i) => {
        if (!paths[i]) return;
        paths[i].setAttribute("fill", layer.color);
        paths[i].setAttribute("fill-opacity", layer.opacity);
    });

    function generateWavePath(amplitude, frequency, clock, yOffset) {
        const points = [];
        const baseY  = SVG_HEIGHT - yOffset;
        const W      = WAVE_WIDTH;

        // 파도 주기를 모바일 너비 기준으로 유지해 시각적 밀도 동일하게
        const freqScale = W / MOBILE_WIDTH;

        points.push(`M -20 ${baseY}`);

        for (let x = -20; x <= W + 20; x += 4) {
            const angle = (x / W) * Math.PI * frequency * freqScale * 2 + clock;
            const y     = baseY + amplitude * Math.sin(angle);
            points.push(`L ${x} ${y}`);
        }

        points.push(`L ${W + 20} ${SVG_HEIGHT}`);
        points.push(`L -20 ${SVG_HEIGHT}`);
        points.push("Z");

        return points.join(" ");
    }

    // 리사이즈 시 파도 너비 갱신
    window.addEventListener("resize", () => {
        WAVE_WIDTH = window.innerWidth;
        if (svg) svg.setAttribute("viewBox", `0 0 ${WAVE_WIDTH} ${SVG_HEIGHT}`);
    });

    // viewBox 초기값
    if (svg) svg.setAttribute("viewBox", `0 0 ${WAVE_WIDTH} ${SVG_HEIGHT}`);

    let clock = 0;
    let rafId = null;

    function animate() {
        clock += 0.015;

        WAVE_LAYERS.forEach((layer, i) => {
            if (!paths[i]) return;
            const d = generateWavePath(
                layer.amplitude,
                layer.frequency,
                clock * layer.speed,
                layer.yOffset + (SVG_HEIGHT - FILL_HEIGHT)
            );
            paths[i].setAttribute("d", d);
        });

        rafId = requestAnimationFrame(animate);
    }

    rafId = requestAnimationFrame(animate);

    window.addEventListener("pagehide", () => {
        if (rafId) cancelAnimationFrame(rafId);
    });
})();