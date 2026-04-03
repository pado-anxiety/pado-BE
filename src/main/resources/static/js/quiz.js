(function () {
    let currentIndex = 0;
    const scores = [];
    let selected = false; // 중복 클릭 방지

    const totalQuestions = QUESTIONS.length;

    const $number   = document.getElementById("question-number");
    const $progress = document.getElementById("progress-bar");
    const $situation = document.getElementById("situation");
    const $choices  = document.getElementById("choices");
    const $form     = document.getElementById("quiz-form");

    function render(index) {
        const q = QUESTIONS[index];
        selected = false;

        // 진행 상태
        $number.textContent = `${index + 1} / ${totalQuestions}`;

        // 프로그레스 바
        const percent = ((index + 1) / totalQuestions) * 100;
        $progress.style.width = `${percent}%`;

        // 질문
        $situation.textContent = q.situation;

        // 선택지 렌더링
        $choices.innerHTML = "";
        q.choices.forEach((choice, i) => {
            const btn = document.createElement("button");
            btn.type = "button";
            btn.textContent = choice.label;
            btn.dataset.score = choice.score;
            btn.dataset.index = i;
            btn.className = [
                "w-full text-left px-4 py-3.5 rounded-xl border",
                "text-[15px] leading-6 tracking-tight",
                "transition-all duration-200",
                "bg-surface border-border-subtle text-text-primary",
                "hover:border-text-tertiary hover:bg-elevated",
                "active:scale-[0.97]",
                "focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-btn-dark"
            ].join(" ");
            btn.style.transitionTimingFunction = "cubic-bezier(0.25,1,0.5,1)";

            btn.addEventListener("click", () => handleSelect(choice.score, i));
            $choices.appendChild(btn);
        });
    }

    function handleSelect(score, choiceIndex) {
        if (selected) return;
        selected = true;

        // 선택된 버튼 스타일
        const buttons = $choices.querySelectorAll("button");
        buttons[choiceIndex].className = [
            "w-full text-left px-4 py-3.5 rounded-xl border",
            "text-[15px] leading-6 tracking-tight",
            "transition-all duration-200",
            "bg-btn-dark border-btn-dark text-[#F2F2F8] font-semibold scale-[0.97]"
        ].join(" ");

        scores.push(score);

        setTimeout(() => {
            if (currentIndex < totalQuestions - 1) {
                currentIndex++;
                render(currentIndex);
            } else {
                submitResult();
            }
        }, 400);
    }

    function submitResult() {
        const total = scores.reduce((a, b) => a + b, 0);

        // 각 문항 점수를 hidden input으로 추가
        scores.forEach((score, i) => {
            const input = document.createElement("input");
            input.type = "hidden";
            input.name = `q${i + 1}`;
            input.value = score;
            $form.appendChild(input);
        });

        // 총점도 함께 전송
        const totalInput = document.createElement("input");
        totalInput.type = "hidden";
        totalInput.name = "total";
        totalInput.value = total;
        $form.appendChild(totalInput);

        $form.submit();
    }

    // 첫 번째 질문 렌더링
    render(0);
})();