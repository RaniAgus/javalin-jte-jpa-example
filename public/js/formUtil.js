
/**
 * @desc This function is used to sync the form input data with the form model
 * @param {string} fileInputId 
 * @param {string} imgId 
 */
const syncFileInputWithImage = (fileInputId, imgId) => {
  document.getElementById(fileInputId).onchange = function () {
    document.getElementById(imgId).src = window.URL.createObjectURL(this.files[0]);
  }
}

const FormSubmitHandlerBuilder = function (formId) {
  const submitHandlers = []
  return {
    /**
    * @desc This function is used to remove the date input field from the form if
    * the date is not set
    * @param {string} dateInputId
    */
    addEmptyDateInputHandler: function (dateInputId) {
      submitHandlers.push(() => {
        if (document.getElementById(dateInputId).value === '') {
          document.getElementById(dateInputId).remove();
        }
      });
      return this;
    },
    /**
     * @desc This function is used to remove the file input field from the form if
     * the file is not set
     * @param {string} fileInputId
     */
    addEmptyFileInputHandler: function (fileInputId) {
      submitHandlers.push(() => {
        if (document.getElementById(fileInputId).files.length === 0) {
          document.getElementById(fileInputId).remove();
        }
      });
      return this;
    },
    /**
     * @desc This function is used to register a submit handler for the form
     * considering all the registered handlers
     */
    build: function () {
      document.getElementById(formId).onsubmit = () => {
        submitHandlers.forEach(handler => handler());
      }
    }
  };
}
