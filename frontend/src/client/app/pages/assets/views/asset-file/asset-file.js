import { previewSlides, statusOptions } from '../../../../helper/data/common';

class AssetFileController {
  constructor ($scope, $state) {
    this.statusOptions = statusOptions;
    this.fileUpload = {title: 'Chartered Case Study'};
    this.fileUpload.status = this.statusOptions[0];
    
    this.slides = previewSlides;
    this.activeSlide = this.slides[0];
    this.showInputHeader = false;

    this.activateEditHeader = () => {
      this.showInputHeader = true;
    };

    this.selectedSlide = (ind) => {
      this.activeSlide = this.slides[ind];
    };

    this.log = () => {
      console.log(this.fileUpload)
    }
  }
}
  
  AssetFileController.$inject = ['$scope', '$state'];
  
  module.exports = AssetFileController;