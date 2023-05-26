const slider = document.querySelector(".slider");
firstCard = slider.querySelectorAll(".top-pick-card")[0];
arrowIcons = document.querySelectorAll(".top-picks-btn");

let firstCardWidth = firstCard.clientWidth + 15;


arrowIcons.forEach(icon =>{
    icon.addEventListener("click", () =>{
        slider.scrollLeft += icon.id == "btn-left" ? -firstCardWidth : firstCardWidth;
    })
});

