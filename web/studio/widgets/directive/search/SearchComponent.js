/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// identity function for calling harmony imports with the correct context
/******/ 	__webpack_require__.i = function(value) { return value; };
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, {
/******/ 				configurable: false,
/******/ 				enumerable: true,
/******/ 				get: getter
/******/ 			});
/******/ 		}
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 1);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
    value: true
});

var _slicedToArray = function () { function sliceIterator(arr, i) { var _arr = []; var _n = true; var _d = false; var _e = undefined; try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"]) _i["return"](); } finally { if (_d) throw _e; } } return _arr; } return function (arr, i) { if (Array.isArray(arr)) { return arr; } else if (Symbol.iterator in Object(arr)) { return sliceIterator(arr, i); } else { throw new TypeError("Invalid attempt to destructure non-iterable instance"); } }; }();

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

var ALL_NODES = [];
var BUSINESS_DIMENSION = "BUSINESS_DIMENSION";

/**
 * Calles the Container node constructor if it has a proper business category
 * @param {*} subContainer 
 * @param {*} depth 
 */
function createContainerNode(subContainer) {
    var depth = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : 0;

    return new ContainerNode(subContainer, depth);
}

var ContainerNode = function ContainerNode(_ref) {
    var _this = this;

    var businessCategory = _ref.businessCategory,
        id = _ref.id,
        label = _ref.label,
        qualifiedId = _ref.qualifiedId,
        searchBoost = _ref.searchBoost,
        container = _ref.container;
    var depth = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : 0;

    _classCallCheck(this, ContainerNode);

    this.type = "ContainerNode";

    _.assign(this, { businessCategory: businessCategory, id: id, label: label, qualifiedId: qualifiedId, searchBoost: searchBoost, depth: depth });
    if (!container || container.length === 0) return;
    this.nodes = {};
    var newDepth = depth++;
    function addDepth(subContainer) {
        return createContainerNode(subContainer, newDepth);
    }
    container.map(addDepth).filter(function (node) {
        return node !== null;
    }).forEach(function (node) {
        if (_this.nodes[node.label]) {
            console.warn("ContainerNode::The node with label " + label + " already exists, skipping");
            return;
        };
        _this.nodes[node.label] = node;
        ALL_NODES.push(node);
    });
};

var ARROW = "»";

var BizDimensionEntity = function () {
    function BizDimensionEntity(_ref2) {
        var label = _ref2.label,
            bizDimensionNode = _ref2.bizDimensionNode;

        _classCallCheck(this, BizDimensionEntity);

        this.type = "BizDimensionEntity";
        this.hasAddedLazyEntries = false;

        this.label = label;
        this.bizDimensionNode = bizDimensionNode;
    }

    _createClass(BizDimensionEntity, [{
        key: "createLazyIndex",
        value: function createLazyIndex() {
            if (this.hasAddedLazyEntries || !this.bizDimensionNode || !this.bizDimensionNode.nodes) return [];
            var bizDimEnt = this;
            var index = Object.keys(this.bizDimensionNode.nodes).map(function (suffix) {
                return new BizDimEntSubNode({ bizDimEnt: bizDimEnt, suffix: suffix });
            });
            this.hasAddedLazyEntries = true;
            return index;
        }
    }]);

    return BizDimensionEntity;
}();

;

var BizDimEntSubNode = function BizDimEntSubNode(_ref3) {
    var bizDimEnt = _ref3.bizDimEnt,
        suffix = _ref3.suffix;

    _classCallCheck(this, BizDimEntSubNode);

    this.type = "BizDimEntSubNode";

    this.bizDimEnt = bizDimEnt;
    this.label = this.bizDimEnt.label + " (" + suffix + ")";
};

/**
 * First level nodes require a business category
 * @param {*} container 
 */


function createBizCategoryNodes(container) {
    if (!container.businessCategory) return null;
    return createContainerNode(container);
}

var ContainerGraph = function () {
    function ContainerGraph(ebTemplate) {
        var _this2 = this;

        _classCallCheck(this, ContainerGraph);

        var container = ebTemplate.dataDefinition.container;

        this.rootNodes = {};
        container.map(createBizCategoryNodes).filter(function (node) {
            return node !== null;
        }).forEach(function (node) {
            if (_this2.rootNodes[node.label]) {
                console.warn("ContainerGraph::The node with label " + node.label + " already exists, skipping");
                return;
            };
            _this2.rootNodes[node.label] = node;
            ALL_NODES.push(node);
        });
        this.ALL_NODES = ALL_NODES;
        this.bizDimensionNodes = {};
        ALL_NODES.filter(function (node) {
            return node.businessCategory === BUSINESS_DIMENSION;
        }).forEach(function (node) {
            if (_this2.bizDimensionNodes[node.id]) {
                console.warn("ContainerGraph::A business dimension node with label " + node.label + " already exists, skipping");
                return;
            }
            _this2.bizDimensionNodes[node.id] = node;
        });
    }

    _createClass(ContainerGraph, [{
        key: "createBizDimEntity",
        value: function createBizDimEntity(_ref4) {
            var label = _ref4.label,
                businessDimensionType = _ref4.businessDimensionType;

            var bizDimensionNode = this.bizDimensionNodes[businessDimensionType];
            return new BizDimensionEntity({ label: label, bizDimensionNode: bizDimensionNode });
        }
        //Only two deep

    }, {
        key: "createIndex",
        value: function createIndex() {
            return _.values(this.rootNodes);
        }
    }, {
        key: "matches",
        value: function matches(tokens) {}
    }]);

    return ContainerGraph;
}();

var LABEL = "label";

//If select and enter fall within this timediff (in ms), do not fire the onSearch event
var SELECT_ENTER_DISP = 10;

function processBizDimEnts(value, businessDimensionType) {
    var index = [];
    value.forEach(function (entity) {
        var name = entity.name,
            shortName = entity.shortName;

        if (!name) return;
        name = name.trim();
        shortName = String(shortName || name).trim();
        if (name) index.push({ label: name, businessDimensionType: businessDimensionType });
        if (shortName !== name) index.push({ label: shortName, businessDimensionType: businessDimensionType });
    });
    return index;
}

var SearchComponent = exports.SearchComponent = function () {
    function SearchComponent(_ref5) {
        var _this3 = this;

        var mountPoint = _ref5.mountPoint,
            asyncData = _ref5.asyncData;

        _classCallCheck(this, SearchComponent);

        this.lastSelected = "";
        this.local = [];
        this.enteredQuery = "";
        this.selectedTs = new Date();

        this.suggestionTemplate = function (node) {
            var label = node.label,
                id = node.id;

            var innerContent = label;
            if (node instanceof BizDimensionEntity) {
                var bizDimensionNode = node.bizDimensionNode;

                var dimension = bizDimensionNode.label;
                innerContent = "<span>" + label + "</span><span class=\"pull-right eb-tt-biz-dimension\">" + dimension + "</span>";
            }
            return "\n        <div data-id=\"" + id + "\" class=\"container-fluid\">\n            " + innerContent + "\n        </div>\n        ";
        };

        this.updateBizEntity = function (content, key) {
            var idx = processBizDimEnts(content, key).map(_this3._createBizDimEntity);
            console.log({ idx: idx });
            _this3.addToIdx(idx);
        };

        this.onTrueEnter = function (enteredQuery) {
            console.warn("onTrueEnter not set", { enteredQuery: enteredQuery });
        };

        this.onEnter = function (e) {
            var keyCode = e.which || e.keyCode;
            if (keyCode === 13) {
                e.preventDefault();
                var value = e.target.value;

                value = value.trim();
                _this3.enteredQuery = value;
                var enterTs = new Date();
                var timeDiff = enterTs - _this3.selectedTs;
                if (SELECT_ENTER_DISP < timeDiff) {
                    _this3.onTrueEnter(_this3.enteredQuery);
                }
                return false;
            }
        };

        this.onSelect = function (e) {
            var value = e.target.value;

            value = value.trim();
            _this3.selectedTs = new Date();
            _this3.lastSelected = value;
            console.log("onSelect", value);
            var node = _this3.hound.get([value])[0];
            if (!node) return;
            console.log({ node: node });
            if (node instanceof BizDimensionEntity) {
                //Add lazy entries now
                _this3.addToIdx(node.createLazyIndex());
            }
        };

        this.onChange = function (e) {
            var value = e.target.value;

            value = value.trim();
            console.log("onChange", value);
        };

        this._createBizDimEntity = function (payload) {
            return _this3.containerGraph.createBizDimEntity(payload);
        };

        this.hound = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace(LABEL),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            local: this.local,
            identify: function identify(obj) {
                return obj.label;
            }
        });
        this.$e = $(mountPoint);
        this.$e.typeahead({
            hint: true,
            highlight: true,
            minLength: 1
        }, {
            name: 'entities',
            source: this.hound,
            limit: 10,
            display: LABEL,
            templates: {
                suggestion: this.suggestionTemplate
            }
        }).bind("typeahead:select", this.onSelect)
        //.bind("typeahead:change",this.onChange)
        .keydown(this.onEnter);
        //Kick off async processing
        asyncData.template.then(function (ebTemplate) {
            _this3.updateTemplate(ebTemplate);
            _(asyncData).omit(["template"]).entries().forEach(function (_ref6) {
                var _ref7 = _slicedToArray(_ref6, 2),
                    key = _ref7[0],
                    promise = _ref7[1];

                promise.then(function (content) {
                    return _this3.updateBizEntity(content, key);
                });
            });
        });
    }

    _createClass(SearchComponent, [{
        key: "updateTemplate",
        value: function updateTemplate(ebTemplate) {
            if (this.containerGraph) {
                console.warn("containerGraph has already been set, skipping");
                return;
            }
            this.containerGraph = new ContainerGraph(ebTemplate);
            var idx = this.containerGraph.createIndex();
            this.addToIdx(idx);
        }
    }, {
        key: "addToIdx",
        value: function addToIdx(items) {
            this.hound.add(items);
        }
        /**
         * This fires when an enter is enter independent of a select
         */

    }]);

    return SearchComponent;
}();

;

/***/ }),
/* 1 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _ = __webpack_require__(0);

window.enablix = window.enablix || {};

window.enablix.SearchComponent = _.SearchComponent;

/***/ })
/******/ ]);