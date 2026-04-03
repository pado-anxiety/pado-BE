(function () {
    const el = document.getElementById("counter");
    if (!el) return;

    const target = parseInt(el.dataset.target, 10);
    if (!target || target === 0) return;

    const duration = 2400;
    const steps    = 60;
    let step = 0;

    const timer = setInterval(() => {
        step++;
        const t      = step / steps;
        const eased  = 1 - Math.pow(1 - t, 4); // ease-out-quart
        const current = Math.min(Math.round(target * eased), target);

        el.textContent = current >= 10000
            ? `${(current / 10000).toFixed(1)}만`
            : current.toLocaleString();

        if (step >= steps) clearInterval(timer);
    }, duration / steps);
})();