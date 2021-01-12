function markRead(id) {
    $.ajax({
        url: "/ajax/read/" + id,
        type: "POST",
        headers: {"X-CSRF-TOKEN": $("input[name='_csrf']").val()},
        success: function (data) {
            $("#message-" + id + "-tab>span").removeClass("text-danger");
            $("#unread").text(data);
        }
    })
}