(function () {
    const MAX_SCORE = 21;

    const myBar   = document.getElementById("my-bar");
    const avgBar  = document.getElementById("avg-bar");
    const compareText = document.getElementById("compare-text");

    const myPercent  = Math.max((SCORE / MAX_SCORE) * 100, 8);
    const avgPercent = Math.max((AVERAGE / MAX_SCORE) * 100, 8);

    // 페이지 로드 후 살짝 딜레이 줘야 transition 작동
    requestAnimationFrame(() => {
        setTimeout(() => {
            myBar.style.width  = `${myPercent}%`;
            avgBar.style.width = `${avgPercent}%`;
        }, 100);
    });

    // 비교 텍스트
    const diff = SCORE - AVERAGE;
    if (diff === 0) {
        compareText.textContent = "평균과 동일한 점수예요.";
    } else if (diff > 0) {
        compareText.textContent = `평균보다 ${diff.toFixed(1)}점 높아요.`;
    } else {
        compareText.textContent = `평균보다 ${Math.abs(diff).toFixed(1)}점 낮아요.`;
    }
})();