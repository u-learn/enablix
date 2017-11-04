class AssetsController {
    constructor ($state, $window) {
      this.state = $state;
      this.showPublishOptions = false;
      this.showDeletePrompt = false;

      this.goBackHome = () => {
        $state.go('home');
      }

      this.previewState = () => {
        //go back to original state
      }

      if($state.current.name.includes('edit')){
        this.showPublishOptions = true;
      } 
      
      this.editState = () => {
        const editState = this.state.current.name + '-edit';
        $state.go(editState);
        this.showPublishOptions = true;
      };

      this.cancelState = () => {
        // const backState = this.state.current.name.slice(-5);
        // $state.go(backState);
        $window.history.back();
        this.showPublishOptions = false;
      }

      this.deletePrompt = () => {
        const currentState = this.state.current.name;
        this.showDeletePrompt = true;
      };

      this.deleteAction = (action) => {
        if(action === 'delete'){
          //delete
        } else {
          //close
        }
        this.showDeletePrompt = false;
      }

      this.emailAction = (action) => {
        if(action === 'send'){
          //delete
        } else {
          //close
        }
        this.showEmailPrompt = false;
      }

      this.showActionConfirmed = {attachment: false};
      this.confirmationActionImg = {attachment: {link: 'assets/images/buttons/copied_url_btn.png'}};
      this.confirmAction = (actionType) => {
        if (actionType === 'attachment'){
          this.showActionConfirmed.attachment = true;
          //https://stackoverflow.com/questions/400212/how-do-i-copy-to-the-clipboard-in-javascript
        }
      }

      this.emailPrompt = () => {
        this.showEmailPrompt = true;
      };

      this.email = {};
    }

  }
  
  AssetsController.$inject = ['$state', '$window'];
  
  module.exports = AssetsController;
  