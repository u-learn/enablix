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

  const previewSlides = [
    {id: 1, url: 'assets/images/covers/asset-file-1.jpg'},
    {id: 2, url: 'assets/images/covers/asset-file-2.png'},
    {id: 3, url: 'assets/images/covers/asset-file-4.png'},
    {id: 4, url: 'assets/images/covers/asset-file-5.jpg'},
    {id: 5, url: 'assets/images/covers/asset-file-6.png'}
  ];

  const statusOptions = [
    {type: "Pending"},
    {type: "Approved"},
    {type: "Draft"}
  ]