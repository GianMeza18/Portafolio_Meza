document.addEventListener('DOMContentLoaded', () => {
    window.AOS?.init({ duration: 700, once: true, offset: 60 });
    const loader = document.querySelector('.page-loader');
    const navbar = document.querySelector('.site-nav');
    const backTop = document.querySelector('#back-top');
    const filterButtons = document.querySelectorAll('[data-filter]');
    const evidenceCards = document.querySelectorAll('.evidence-card');

    window.setTimeout(() => loader?.classList.add('is-hidden'), 450);

    const updateScrollState = () => {
        navbar?.classList.toggle('is-scrolled', window.scrollY > 24);
        backTop?.classList.toggle('is-visible', window.scrollY > 500);
    };
    window.addEventListener('scroll', updateScrollState, { passive: true });
    updateScrollState();

    backTop?.addEventListener('click', () => window.scrollTo({ top: 0, behavior: 'smooth' }));

    filterButtons.forEach((button) => {
        button.addEventListener('click', () => {
            filterButtons.forEach((item) => item.classList.remove('active'));
            button.classList.add('active');
            const filter = button.dataset.filter;
            evidenceCards.forEach((card) => {
                const matches = filter === 'all' || card.dataset.status === filter;
                card.classList.toggle('is-filtered', !matches);
            });
        });
    });

    document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
        anchor.addEventListener('click', (event) => {
            const target = document.querySelector(anchor.getAttribute('href'));
            if (target) {
                event.preventDefault();
                target.scrollIntoView({ behavior: 'smooth', block: 'start' });
            }
        });
    });
});
