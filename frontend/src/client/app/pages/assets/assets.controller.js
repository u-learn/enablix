class AssetsController {
    constructor ($state, $window, $timeout) {
      this.state = $state;
      this.showPublishOptions = false;
      this.showDeletePrompt = false;

      this.goBackHome = () => {
        $state.go('home.dashboard');
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

      this.showActionConfirmed = {
        attachment: false,
        copy: false,
        email: false
      };
      this.confirmationActionImg = {attachment: {link: 'assets/images/buttons/copied_url_btn.png'}};
      this.confirmAction = (actionType) => {
          this.showActionConfirmed[actionType] = true;
          //https://stackoverflow.com/questions/400212/how-do-i-copy-to-the-clipboard-in-javascript
          $timeout(() => {
            this.showActionConfirmed[actionType] = false;
          }, 1000);
      }

      this.emailPrompt = () => {
        this.showEmailPrompt = true;
      };

      this.email = {};

      this.btnState = {
        'edit': false,
        'delete': false,
        'copy': false,
        'attach': false,
        'email': false
      };

      this.showBtn = (type) => {
        console.log(type)
        this.btnState[type] = true;
      };

      this.hideBtn = (type) => {
        this.btnState[type] = false;
      };
    }
  }
  
  AssetsController.$inject = ['$state', '$window', '$timeout'];
  
  module.exports = AssetsController;
  