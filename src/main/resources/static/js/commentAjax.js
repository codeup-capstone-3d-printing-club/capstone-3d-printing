function deleteComment(fileId, id) {
    $(".delete-btn").prop("disabled", true)

    $.ajax({
        url: "/files/" + fileId + "/comment/" + id + "/delete?" + $("#delete-form" + id).serialize(),
        type: "POST",
        headers: {"X-CSRF-TOKEN": $("input[name='_csrf']").val()},
        success: function () {
            $("#comments-main-" + id).fadeOut(500);
            $(".delete-btn").prop("disabled", false)
        }
    });
}
function postComment(fileId) {

    $("#post-btn").prop("disabled", true)

    $.ajax({
        url: "/files/" + fileId + "/comment?" + $("#post-form").serialize(),
        type: "POST",
        headers: {"X-CSRF-TOKEN": $("input[name='_csrf']").val()},
        success: function () {

            $("#post-btn").prop("disabled", true);

            $.ajax({
                url: "/files/" + fileId,
                type: "GET",
                headers: {"X-CSRF-TOKEN": $("input[name='_csrf']").val()},
                success: function (data) {

                    let page = document.createElement("html");
                    $(page).html(data);
                    $('#comments-container').html(page.querySelector("#comments-container").innerHTML);

                    $("#post-btn").prop("disabled", false)
                }
            })
        }
    });
}

function postReply(fileId, commentId) {
    $(".reply-btn").prop("disabled", true)

    $.ajax({
        url: "/files/" + fileId + "/comment/" + commentId + "?" + $("#reply-form" + commentId).serialize(),
        type: "POST",
        headers: {"X-CSRF-TOKEN": $("input[name='_csrf']").val()},
        success: function () {

            $(".reply-btn").prop("disabled", true);

            $.ajax({
                url: "/files/" + fileId,
                type: "GET",
                headers: {"X-CSRF-TOKEN": $("input[name='_csrf']").val()},
                success: function (data) {

                    let page = document.createElement("html");
                    $(page).html(data);
                    $('#comments-container').html(page.querySelector("#comments-container").innerHTML);

                    $(".reply-btn").prop("disabled", false);
                }
            });
        }
    });
}