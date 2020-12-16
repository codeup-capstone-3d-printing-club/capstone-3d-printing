window.addEventListener('DOMContentLoaded', function () {
    const client = filestack.init(FILESTACK_API_KEY);
    const options = {
        // displayMode: 'inline',
        // container: '#inline',
        maxFiles: 20,
        uploadInBackground: false,
        onUploadDone: uploadFile
    };
    const picker = client.picker(options);
    const openBtn = document.getElementById('open');
    // const closeBtn = document.getElementById('close');
    openBtn.addEventListener('click', () => picker.open());
    // closeBtn.addEventListener('click', () => picker.close());

    function uploadFile(result) {
        const fileData = result.filesUploaded[0];
        console.log(fileData);
        console.log(fileData.url);
        $('#pickedFile').val(fileData.url);
    }

});