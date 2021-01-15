function allCategories() {
    $(".fileCard").each(function() {
        $(this).removeClass("d-none");
    });

    $("#page-title").text("All Categories");
}

function switchTo(categoryName) {
    $(".fileCard").each(function () {
        if ($(this).hasClass(categoryName)) {
            $(this).removeClass("d-none");
        } else {
            $(this).addClass("d-none");
        }
    });

    $("#page-title").text(categoryName);
}