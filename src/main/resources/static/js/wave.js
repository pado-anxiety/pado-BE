(function () {
    // Ocean palette — 먼 것(index 0)부터 가까운 것(index 4) 순서
    const WAVE_LAYERS = [
        { color: "#BACCEB", opacity: 0.4,  amplitude: 6, frequency: 1.3, speed: 0.3, yOffset: 100 }, // bg-wave (가장 먼)
        { color: "#8AA4D0", opacity: 0.35, amplitude: 8, frequency: 0.9, speed: 0.4, yOffset: 80  }, // horizon
        { color: "#5E7EB4", opacity: 0.35, amplitude: 9, frequency: 1.1, speed: 0.6, yOffset: 60  }, // mid-wave
        { color: "#3A5C96", opacity: 0.35, amplitude: 7, frequency: 1.4, speed: 0.8, yOffset: 45  }, // fore-wave
        { color: "#1E3D72", opacity: 0.35, amplitude: 5, frequency: 1.8, speed: 1.0, yOffset: 30  }, // front-wave (가장 가까운)
    ];

    const WIDTH      = 430;
    const SVG_HEIGHT = 300;
    const FILL_HEIGHT = 260;

    const paths = [
        document.getElementById("wave-0"),
        document.getElementById("wave-1"),
        document.getElementById("wave-2"),
        document.getElementById("wave-3"),
        document.getElementById("wave-4"),
    ];

    // path 초기 색상 적용
    WAVE_LAYERS.forEach((layer, i) => {
        if (!paths[i]) return;
        paths[i].setAttribute("fill", layer.color);
        paths[i].setAttribute("fill-opacity", layer.opacity);
    });

    function generateWavePath(amplitude, frequency, clock, yOffset) {
        const points = [];
        const baseY = SVG_HEIGHT - yOffset;

        points.push(`M -20 ${baseY}`);

        for (let x = -20; x <= WIDTH + 20; x += 4) {
            const angle = (x / WIDTH) * Math.PI * frequency * 2 + clock;
            const y = baseY + amplitude * Math.sin(angle);
            points.push(`L ${x} ${y}`);
        }

        points.push(`L ${WIDTH + 20} ${SVG_HEIGHT}`);
        points.push(`L -20 ${SVG_HEIGHT}`);
        points.push("Z");

        return points.join(" ");
    }

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

    // 페이지 벗어날 때 정리
    window.addEventListener("pagehide", () => {
        if (rafId) cancelAnimationFrame(rafId);
    });
})();