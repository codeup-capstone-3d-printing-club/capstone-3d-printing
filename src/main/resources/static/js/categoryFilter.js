function allCategories() {
    $(".fileCard").each(function() {
        $(this).removeClass("d-none");
    });
}

function switchTo(element) {
    let categoryName = element.dataset.category;
    $(".fileCard").each(function () {
        if ($(this).hasClass(categoryName)) {
            $(this).removeClass("d-none");
        } else {
            $(this).addClass("d-none");
        }
    });
}