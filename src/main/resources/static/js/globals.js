document.addEventListener("DOMContentLoaded", function () {
    const wrapper = document.getElementById("carousel-wrapper");
    const track = document.getElementById("carousel-track");
    const prevBtn = document.getElementById("prev-btn");
    const nextBtn = document.getElementById("next-btn");

    const VISIBLE_ITEMS = 5;
    let badgeWidth = 0;
    let gap = 16; // Tailwind gap-4 (16px)

    function updateBadgeSize() {
        const wrapperWidth = wrapper.clientWidth;
        badgeWidth = (wrapperWidth - gap * (VISIBLE_ITEMS - 1)) / VISIBLE_ITEMS;

        document.querySelectorAll(".carousel-item").forEach((item) => {
            item.style.width = `${badgeWidth}px`;
            item.style.flex = `0 0 ${badgeWidth}px`;
        });

        updateButtonState();
    }

    function updateButtonState() {
        prevBtn.disabled = wrapper.scrollLeft <= 0;
        nextBtn.disabled =
            wrapper.scrollLeft + wrapper.clientWidth >= track.scrollWidth;
    }

    if (nextBtn && prevBtn) {
        nextBtn.addEventListener("click", () => {
            wrapper.scrollBy({ left: badgeWidth + gap, behavior: "smooth" });
            setTimeout(updateButtonState, 300);
        });

        prevBtn.addEventListener("click", () => {
            wrapper.scrollBy({ left: -(badgeWidth + gap), behavior: "smooth" });
            setTimeout(updateButtonState, 300);
        });
    }

    if (wrapper) {
        window.addEventListener("resize", updateBadgeSize);
        updateBadgeSize();
    }
});

function validateInteger(input) {
    input.value = input.value.replace(/[^0-9]/g, "");
}

function previewImage(event) {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            document.getElementById("avatarPreview").src = e.target.result;
        };
        reader.readAsDataURL(file);
    }
}
