(function (root, factory) {
  if (typeof define === 'function' && define.amd) {
    define(['quill'], factory)
  } else if (typeof exports === 'object') {
    module.exports = factory(require('quill'))
  } else {
    root.Requester = factory(root.Quill)
  }
}(this, function (Quill) {
  'use strict'

  var app
  // declare ngQuill module
  app = angular.module('ngQuill', [])

  app.provider('ngQuillConfig', function () {
    var config = {
      modules: {
        toolbar: [
          ['bold', 'italic', 'underline', 'strike'],        // toggled buttons
          ['blockquote', 'code-block'],

          [{ 'header': 1 }, { 'header': 2 }],               // custom button
															// values
          [{ 'list': 'ordered' }, { 'list': 'bullet' }],
          [{ 'script': 'sub' }, { 'script': 'super' }],      // superscript/subscript
          [{ 'indent': '-1' }, { 'indent': '+1' }],          // outdent/indent
          [{ 'direction': 'rtl' }],                         // text direction

          [{ 'size': ['small', false, 'large', 'huge'] }],  // custom dropdown
          [{ 'header': [1, 2, 3, 4, 5, 6, false] }],

          [{ 'color': [] }, { 'background': [] }],          // dropdown with
															// defaults from
															// theme
          [{ 'font': [] }],
          [{ 'align': [] }],

          ['clean'],                                         // remove
																// formatting
																// button

          ['link', 'image', 'video']                         // link and
																// image, video
        ]
      },
      theme: 'snow',
      placeholder: 'Insert text here ...',
      readOnly: false,
      boundary: document.body
    }

    this.set = function (customConf) {
      customConf = customConf || {}

      if (customConf.modules) {
        config.modules = customConf.modules
      }
      if (customConf.theme) {
        config.theme = customConf.theme
      }
      if (customConf.placeholder) {
        config.placeholder = customConf.placeholder
      }
      if (customConf.boundary) {
        config.boundary = customConf.boundary
      }
      if (customConf.readOnly) {
        config.readOnly = customConf.readOnly
      }
      if (customConf.formats) {
        config.formats = customConf.formats
      }
    }

    this.$get = function () {
      return config
    }
  })

  app.directive('ngQuillEditor', ['ngQuillConfig', '$timeout', function(ngQuillConfig, $timeout) {
	return {
    scope: {
      'modules': '=',
      'theme': '@?',
      'readOnly': '=?',
      'formats': '=?',
      'placeholder': '=?',
      'onEditorCreated': '&?',
      'onContentChanged': '&?',
      'onSelectionChanged': '&?',
      'ngModel': '=',
      'maxLength': '=?',
      'minLength': '=?'
    },
    require: 'ngModel',
    transclude: {
      'toolbar': '?ngQuillToolbar'
    },
    template: '<div class="ng-hide" ng-show="ready"><ng-transclude ng-transclude-slot="toolbar"></ng-transclude></div>',
    link: {
    	pre: function(scope, elem, attr) {
    		
    	},
    	post: function(scope, elem, attr, ngModelCtrl, $transclude) {
    
    		scope.config = {
	          theme: scope.theme || ngQuillConfig.theme,
	          readOnly: scope.readOnly || ngQuillConfig.readOnly,
	          modules: scope.modules || ngQuillConfig.modules,
	          formats: scope.formats || ngQuillConfig.formats,
	          placeholder: scope.placeholder || ngQuillConfig.placeholder,
	          boundary: ngQuillConfig.boundary
	        }
	
    		  function validate(text) {
    	        if (scope.maxLength) {
    	          if (text.length > scope.maxLength + 1) {
    	            ngModelCtrl.$setValidity('maxlength', false)
    	          } else {
    	            ngModelCtrl.$setValidity('maxlength', true)
    	          }
    	        }

    	        if (scope.minLength > 1) {
    	          // validate only if text.length > 1
    	          if (text.length <= scope.minLength && text.length > 1) {
    	            scope.ngModelCtrl.$setValidity('minlength', false)
    	          } else {
    	            scope.ngModelCtrl.$setValidity('minlength', true)
    	          }
    	        }
    	      }
    		
    	      function _initEditor() {
    	    	  
    	        var $editorElem = angular.element('<div></div>'),
    	            container = elem.children()

    	        scope.editorElem = $editorElem[0]

    	        // set toolbar to custom one
    	        $transclude(function(clone){
    	            if(clone.length){
    	            	scope.config.modules.toolbar = container.find('ng-quill-toolbar').children()[0]
    	            }
    	        });

    	        container.append($editorElem)

    	        scope.editor = new Quill(scope.editorElem, scope.config)
    	        if (scope.ngModel) {
    	      	  scope.editor.pasteHTML(scope.ngModel);
    	        }

    	        scope.ready = true

    	        // mark model as touched if editor lost focus
    	        scope.editor.on('selection-change', function (range, oldRange, source) {
    	          if (scope.onSelectionChanged) {
    	            scope.onSelectionChanged({
    	              editor: scope.editor,
    	              oldRange: oldRange,
    	              range: range,
    	              source: source
    	            })
    	          }

    	          if (range) {
    	            return
    	          }
    	          
    	          scope.$applyAsync(function () {
    	            ngModelCtrl.$setTouched()
    	          })
    	          
    	        })

    	        // update model if text changes
    	        scope.editor.on('text-change', function (delta, oldDelta, source) {
    	          var html = scope.editorElem.children[0].innerHTML
    	          var text = scope.editor.getText()

    	          if (html === '<p><br></p>') {
    	            html = null
    	          }
    	          validate(text)

    	          if (!scope.modelChanged) {
    	            scope.$applyAsync(function () {
    	              scope.editorChanged = true

    	              ngModelCtrl.$setViewValue(html)

    	              if (scope.onContentChanged) {
    	                scope.onContentChanged({
    	                  editor: scope.editor,
    	                  html: html,
    	                  text: text,
    	                  delta: delta,
    	                  oldDelta: oldDelta,
    	                  source: source
    	                })
    	              }
    	            })
    	          }
    	          scope.modelChanged = false
    	        })

    	        // set initial content
    	        if (scope.content) {
    	          scope.modelChanged = true

    	          scope.editor.pasteHTML(content)
    	        }

    	        // provide event to get informed when editor is created -> pass
				// editor object.
    	        if (scope.onEditorCreated) {
    	          scope.onEditorCreated({editor: scope.editor})
    	        }
    	      }
    	      
    	      _initEditor();
    	}
    },
    controllerAs: '$ctrl',
    controller: ['$scope', '$element', '$timeout', '$transclude', 'ngQuillConfig', function ($scope, $element, $timeout, $transclude, ngQuillConfig) {
      $scope.content = null;
      $scope.modelChanged = false;
      $scope.editorChanged = false;
      
      $scope.$watch('ngModel', function(currentValue, previousValue) {

    	  if (currentValue !== previousValue) {
              $scope.content = currentValue

              if ($scope.editor && !$scope.editorChanged) {
                $scope.modelChanged = true
                if ($scope.content) {
                	$scope.editor.pasteHTML($scope.content)
                } else {
                	$scope.editor.setText('')
                }
              }
              $scope.editorChanged = false
            }
		});
      
      $scope.$watch('readOnly', function(currentValue, previousValue) {
    	  $scope.editor.enable(!currentValue)
      });
      
    }]
  }
  }])
}))
