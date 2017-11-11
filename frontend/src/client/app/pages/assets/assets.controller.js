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
        attach: false,
        // copy: false,
        // email: false
      };

      this.confirmAction = (actionType) => {
          this.showActionConfirmed[actionType] = true;
          //https://stackoverflow.com/questions/400212/how-do-i-copy-to-the-clipboard-in-javascript
          $timeout(() => {
            this.showActionConfirmed[actionType] = false;
          }, 1000);
      }

      this.externalActions = (actionType) => {
        this.actionBtnState[actionType] = false;
        if(actionType === 'website'){
          //go to href url
          window.open("https://medium.com/swlh/premature-scaling-why-it-kills-startups-and-how-to-avoid-it-500677f45024");
        } else if (actionType === 'download'){
          //download asset
        }
      }

      this.emailPrompt = () => {
        this.showEmailPrompt = true;
      };

      this.email = {};

      this.assetTypeBtn = (() => {
        if (this.state.current.name.includes("text")) {
          return "copy";
        } else if (this.state.current.name.includes("url")) {
          return "website";
        } else {
          return "download";
        }
      })();

      this.actionBtnState = {
        'edit': false,
        'delete': false,
        'copy': false,
        'attach': false,
        'email': false,
        'website': false
      };

      this.showBtn = (type) => {
        this.actionBtnState[type] = true;
      };

      this.hideBtn = (type) => {
        this.actionBtnState[type] = false;
      };

      this.showNotifications = () => {
        this.showNotificationsPopup = true;
      };

      this.hideNotifications = () => {
        this.showNotificationsPopup = false;
      };
  
    }
  }
  
  AssetsController.$inject = ['$state', '$window', '$timeout'];
  
  module.exports = AssetsController;
  