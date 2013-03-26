//>>built
require({cache:{"url:dijit/templates/TreeNode.html":"<div class=\"dijitTreeNode\" role=\"presentation\"\n\t><div data-dojo-attach-point=\"rowNode\" class=\"dijitTreeRow\" role=\"presentation\" data-dojo-attach-event=\"onmouseenter:_onMouseEnter, onmouseleave:_onMouseLeave, onclick:_onClick, ondblclick:_onDblClick\"\n\t\t><img src=\"${_blankGif}\" alt=\"\" data-dojo-attach-point=\"expandoNode\" class=\"dijitTreeExpando\" role=\"presentation\"\n\t\t/><span data-dojo-attach-point=\"expandoNodeText\" class=\"dijitExpandoText\" role=\"presentation\"\n\t\t></span\n\t\t><span data-dojo-attach-point=\"contentNode\"\n\t\t\tclass=\"dijitTreeContent\" role=\"presentation\">\n\t\t\t<img src=\"${_blankGif}\" alt=\"\" data-dojo-attach-point=\"iconNode\" class=\"dijitIcon dijitTreeIcon\" role=\"presentation\"\n\t\t\t/><span data-dojo-attach-point=\"labelNode\" class=\"dijitTreeLabel\" role=\"treeitem\" tabindex=\"-1\" aria-selected=\"false\" data-dojo-attach-event=\"onfocus:_onLabelFocus\"></span>\n\t\t</span\n\t></div>\n\t<div data-dojo-attach-point=\"containerNode\" class=\"dijitTreeContainer\" role=\"presentation\" style=\"display: none;\"></div>\n</div>\n","url:dijit/templates/Tree.html":"<div class=\"dijitTree dijitTreeContainer\" role=\"tree\"\n\tdata-dojo-attach-event=\"onkeypress:_onKeyPress\">\n\t<div class=\"dijitInline dijitTreeIndent\" style=\"position: absolute; top: -9999px\" data-dojo-attach-point=\"indentDetector\"></div>\n</div>\n"}});define("dijit/Tree",["dojo/_base/array","dojo/_base/connect","dojo/cookie","dojo/_base/declare","dojo/_base/Deferred","dojo/DeferredList","dojo/dom","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/_base/event","dojo/fx","dojo/_base/kernel","dojo/keys","dojo/_base/lang","dojo/topic","./focus","./registry","./_base/manager","./_Widget","./_TemplatedMixin","./_Container","./_Contained","./_CssStateMixin","dojo/text!./templates/TreeNode.html","dojo/text!./templates/Tree.html","./tree/TreeStoreModel","./tree/ForestStoreModel","./tree/_dndSelector"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c,_d,_e,_f,_10,_11,_12,_13,_14,_15,_16,_17,_18,_19,_1a,_1b,_1c,_1d){var _1e=_4("dijit._TreeNode",[_14,_15,_16,_17,_18],{item:null,isTreeNode:true,label:"",_setLabelAttr:{node:"labelNode",type:"innerText"},isExpandable:null,isExpanded:false,state:"UNCHECKED",templateString:_19,baseClass:"dijitTreeNode",cssStateNodes:{rowNode:"dijitTreeRow"},_setTooltipAttr:{node:"rowNode",type:"attribute",attribute:"title"},buildRendering:function(){this.inherited(arguments);this._setExpando();this._updateItemClasses(this.item);if(this.isExpandable){this.labelNode.setAttribute("aria-expanded",this.isExpanded);}this.setSelected(false);},_setIndentAttr:function(_1f){var _20=(Math.max(_1f,0)*this.tree._nodePixelIndent)+"px";_a.set(this.domNode,"backgroundPosition",_20+" 0px");_a.set(this.rowNode,this.isLeftToRight()?"paddingLeft":"paddingRight",_20);_1.forEach(this.getChildren(),function(_21){_21.set("indent",_1f+1);});this._set("indent",_1f);},markProcessing:function(){this.state="LOADING";this._setExpando(true);},unmarkProcessing:function(){this._setExpando(false);},_updateItemClasses:function(_22){var _23=this.tree,_24=_23.model;if(_23._v10Compat&&_22===_24.root){_22=null;}this._applyClassAndStyle(_22,"icon","Icon");this._applyClassAndStyle(_22,"label","Label");this._applyClassAndStyle(_22,"row","Row");},_applyClassAndStyle:function(_25,_26,_27){var _28="_"+_26+"Class";var _29=_26+"Node";var _2a=this[_28];this[_28]=this.tree["get"+_27+"Class"](_25,this.isExpanded);_8.replace(this[_29],this[_28]||"",_2a||"");_a.set(this[_29],this.tree["get"+_27+"Style"](_25,this.isExpanded)||{});},_updateLayout:function(){var _2b=this.getParent();if(!_2b||!_2b.rowNode||_2b.rowNode.style.display=="none"){_8.add(this.domNode,"dijitTreeIsRoot");}else{_8.toggle(this.domNode,"dijitTreeIsLast",!this.getNextSibling());}},_setExpando:function(_2c){var _2d=["dijitTreeExpandoLoading","dijitTreeExpandoOpened","dijitTreeExpandoClosed","dijitTreeExpandoLeaf"],_2e=["*","-","+","*"],idx=_2c?0:(this.isExpandable?(this.isExpanded?1:2):3);_8.replace(this.expandoNode,_2d[idx],_2d);this.expandoNodeText.innerHTML=_2e[idx];},expand:function(){if(this._expandDeferred){return this._expandDeferred;}if(this._collapseDeferred){this._collapseDeferred.cancel();delete this._collapseDeferred;}this.isExpanded=true;this.labelNode.setAttribute("aria-expanded","true");if(this.tree.showRoot||this!==this.tree.rootNode){this.containerNode.setAttribute("role","group");}_8.add(this.contentNode,"dijitTreeContentExpanded");this._setExpando();this._updateItemClasses(this.item);if(this==this.tree.rootNode){this.tree.domNode.setAttribute("aria-expanded","true");}var def,_2f=_c.wipeIn({node:this.containerNode,duration:_13.defaultDuration,onEnd:function(){def.resolve(true);}});def=(this._expandDeferred=new _5(function(){_2f.stop();}));_2f.play();return def;},collapse:function(){if(this._collapseDeferred){return this._collapseDeferred;}if(this._expandDeferred){this._expandDeferred.cancel();delete this._expandDeferred;}this.isExpanded=false;this.labelNode.setAttribute("aria-expanded","false");if(this==this.tree.rootNode){this.tree.domNode.setAttribute("aria-expanded","false");}_8.remove(this.contentNode,"dijitTreeContentExpanded");this._setExpando();this._updateItemClasses(this.item);var def,_30=_c.wipeOut({node:this.containerNode,duration:_13.defaultDuration,onEnd:function(){def.resolve(true);}});def=(this._collapseDeferred=new _5(function(){_30.stop();}));_30.play();return def;},indent:0,setChildItems:function(_31){var _32=this.tree,_33=_32.model,_34=[];var _35=this.getChildren();_1.forEach(_35,function(_36){_16.prototype.removeChild.call(this,_36);},this);setTimeout(function(){_1.forEach(_35,function(_37){if(!_37._destroyed&&!_37.getParent()){_32.dndController.removeTreeNode(_37);var id=_33.getIdentity(_37.item),ary=_32._itemNodesMap[id];if(ary.length==1){delete _32._itemNodesMap[id];}else{var _38=_1.indexOf(ary,_37);if(_38!=-1){ary.splice(_38,1);}}_37.destroyRecursive();}});},0);this.state="LOADED";if(_31&&_31.length>0){this.isExpandable=true;_1.forEach(_31,function(_39){var id=_33.getIdentity(_39),_3a=_32._itemNodesMap[id],_3b;if(_3a){for(var i=0;i<_3a.length;i++){if(_3a[i]&&!_3a[i].getParent()){_3b=_3a[i];_3b.set("indent",this.indent+1);break;}}}if(!_3b){_3b=this.tree._createTreeNode({item:_39,tree:_32,isExpandable:_33.mayHaveChildren(_39),label:_32.getLabel(_39),tooltip:_32.getTooltip(_39),dir:_32.dir,lang:_32.lang,textDir:_32.textDir,indent:this.indent+1});if(_3a){_3a.push(_3b);}else{_32._itemNodesMap[id]=[_3b];}}this.addChild(_3b);if(this.tree.autoExpand||this.tree._state(_3b)){_34.push(_32._expandNode(_3b));}},this);_1.forEach(this.getChildren(),function(_3c){_3c._updateLayout();});}else{this.isExpandable=false;}if(this._setExpando){this._setExpando(false);}this._updateItemClasses(this.item);if(this==_32.rootNode){var fc=this.tree.showRoot?this:this.getChildren()[0];if(fc){fc.setFocusable(true);_32.lastFocused=fc;}else{_32.domNode.setAttribute("tabIndex","0");}}return new _6(_34);},getTreePath:function(){var _3d=this;var _3e=[];while(_3d&&_3d!==this.tree.rootNode){_3e.unshift(_3d.item);_3d=_3d.getParent();}_3e.unshift(this.tree.rootNode.item);return _3e;},getIdentity:function(){return this.tree.model.getIdentity(this.item);},removeChild:function(_3f){this.inherited(arguments);var _40=this.getChildren();if(_40.length==0){this.isExpandable=false;this.collapse();}_1.forEach(_40,function(_41){_41._updateLayout();});},makeExpandable:function(){this.isExpandable=true;this._setExpando(false);},_onLabelFocus:function(){this.tree._onNodeFocus(this);},setSelected:function(_42){this.labelNode.setAttribute("aria-selected",_42?"true":"false");_8.toggle(this.rowNode,"dijitTreeRowSelected",_42);},setFocusable:function(_43){this.labelNode.setAttribute("tabIndex",_43?"0":"-1");},_onClick:function(evt){this.tree._onClick(this,evt);},_onDblClick:function(evt){this.tree._onDblClick(this,evt);},_onMouseEnter:function(evt){this.tree._onNodeMouseEnter(this,evt);},_onMouseLeave:function(evt){this.tree._onNodeMouseLeave(this,evt);},_setTextDirAttr:function(_44){if(_44&&((this.textDir!=_44)||!this._created)){this._set("textDir",_44);this.applyTextDir(this.labelNode,this.labelNode.innerText||this.labelNode.textContent||"");_1.forEach(this.getChildren(),function(_45){_45.set("textDir",_44);},this);}}});var _46=_4("dijit.Tree",[_14,_15],{store:null,model:null,query:null,label:"",showRoot:true,childrenAttr:["children"],paths:[],path:[],selectedItems:null,selectedItem:null,openOnClick:false,openOnDblClick:false,templateString:_1a,persist:true,autoExpand:false,dndController:_1d,dndParams:["onDndDrop","itemCreator","onDndCancel","checkAcceptance","checkItemAcceptance","dragThreshold","betweenThreshold"],onDndDrop:null,itemCreator:null,onDndCancel:null,checkAcceptance:null,checkItemAcceptance:null,dragThreshold:5,betweenThreshold:0,_nodePixelIndent:19,_publish:function(_47,_48){_10.publish(this.id,_f.mixin({tree:this,event:_47},_48||{}));},postMixInProperties:function(){this.tree=this;if(this.autoExpand){this.persist=false;}this._itemNodesMap={};if(!this.cookieName&&this.id){this.cookieName=this.id+"SaveStateCookie";}this.onLoadDeferred=new _5();this.inherited(arguments);},postCreate:function(){this._initState();if(!this.model){this._store2model();}this.connect(this.model,"onChange","_onItemChange");this.connect(this.model,"onChildrenChange","_onItemChildrenChange");this.connect(this.model,"onDelete","_onItemDelete");this._load();this.inherited(arguments);if(this.dndController){if(_f.isString(this.dndController)){this.dndController=_f.getObject(this.dndController);}var _49={};for(var i=0;i<this.dndParams.length;i++){if(this[this.dndParams[i]]){_49[this.dndParams[i]]=this[this.dndParams[i]];}}this.dndController=new this.dndController(this,_49);}},_store2model:function(){this._v10Compat=true;_d.deprecated("Tree: from version 2.0, should specify a model object rather than a store/query");var _4a={id:this.id+"_ForestStoreModel",store:this.store,query:this.query,childrenAttrs:this.childrenAttr};if(this.params.mayHaveChildren){_4a.mayHaveChildren=_f.hitch(this,"mayHaveChildren");}if(this.params.getItemChildren){_4a.getChildren=_f.hitch(this,function(_4b,_4c,_4d){this.getItemChildren((this._v10Compat&&_4b===this.model.root)?null:_4b,_4c,_4d);});}this.model=new _1c(_4a);this.showRoot=Boolean(this.label);},onLoad:function(){},_load:function(){this.model.getRoot(_f.hitch(this,function(_4e){var rn=(this.rootNode=this.tree._createTreeNode({item:_4e,tree:this,isExpandable:true,label:this.label||this.getLabel(_4e),textDir:this.textDir,indent:this.showRoot?0:-1}));if(!this.showRoot){rn.rowNode.style.display="none";this.domNode.setAttribute("role","presentation");rn.labelNode.setAttribute("role","presentation");rn.containerNode.setAttribute("role","tree");}this.domNode.appendChild(rn.domNode);var _4f=this.model.getIdentity(_4e);if(this._itemNodesMap[_4f]){this._itemNodesMap[_4f].push(rn);}else{this._itemNodesMap[_4f]=[rn];}rn._updateLayout();this._expandNode(rn).then(_f.hitch(this,function(){var _50=this._initialPaths||(this.persist&&this.dndController._getSavedPaths())||[];this._loadCalled=true;_5.when(this.set("paths",_50),_f.hitch(this,function(){this.onLoadDeferred.resolve(true);this.onLoad();}));}));}),_f.hitch(this,function(err){console.error(this,": error loading root: ",err);}));},getNodesByItem:function(_51){if(!_51){return [];}var _52=_f.isString(_51)?_51:this.model.getIdentity(_51);return [].concat(this._itemNodesMap[_52]);},_setSelectedItemAttr:function(_53){this.set("selectedItems",[_53]);},_setSelectedItemsAttr:function(_54){var _55=this;this.onLoadDeferred.then(_f.hitch(this,function(){var _56=_1.map(_54,function(_57){return (!_57||_f.isString(_57))?_57:_55.model.getIdentity(_57);});var _58=[];_1.forEach(_56,function(id){_58=_58.concat(_55._itemNodesMap[id]||[]);});this.set("selectedNodes",_58);}));},_setPathAttr:function(_59){if(_59.length){return this.set("paths",[_59]);}else{return this.set("paths",[]);}},_setPathsAttr:function(_5a){if(!this._loadCalled){if("paths" in this.params||"path" in this.params){this._initialPaths=_5a;}return;}var _5b=this;var dl=new _6(_1.map(_5a,function(_5c){var d=new _5();_5c=_1.map(_5c,function(_5d){return _f.isString(_5d)?_5d:_5b.model.getIdentity(_5d);});if(_5c.length){_5e(_5c,[_5b.rootNode],d);}else{d.reject("Empty path");}return d;}));dl.then(_5f);return dl;function _5e(_60,_61,def){var _62=_60.shift();var _63=_1.filter(_61,function(_64){return _64.getIdentity()==_62;})[0];if(!!_63){if(_60.length){_5b._expandNode(_63).then(function(){_5e(_60,_63.getChildren(),def);});}else{def.resolve(_63);}}else{def.reject("Could not expand path at "+_62);}};function _5f(_65){_5b.set("selectedNodes",_1.map(_1.filter(_65,function(x){return x[0];}),function(x){return x[1];}));};},_setSelectedNodeAttr:function(_66){this.set("selectedNodes",[_66]);},_setSelectedNodesAttr:function(_67){this.dndController.setSelection(_67);},expandAll:function(){var _68=this;function _69(_6a){var def=new dojo.Deferred();_68._expandNode(_6a).then(function(){var _6b=_1.filter(_6a.getChildren()||[],function(_6c){return _6c.isExpandable;}),_6d=_1.map(_6b,_69);new dojo.DeferredList(_6d).then(function(){def.resolve(true);});});return def;};return _69(this.rootNode);},collapseAll:function(){var _6e=this;function _6f(_70){var def=new dojo.Deferred();def.label="collapseAllDeferred";var _71=_1.filter(_70.getChildren()||[],function(_72){return _72.isExpandable;}),_73=_1.map(_71,_6f);new dojo.DeferredList(_73).then(function(){if(!_70.isExpanded||(_70==_6e.rootNode&&!_6e.showRoot)){def.resolve(true);}else{_6e._collapseNode(_70).then(function(){def.resolve(true);});}});return def;};return _6f(this.rootNode);},mayHaveChildren:function(){},getItemChildren:function(){},getLabel:function(_74){return this.model.getLabel(_74);},getIconClass:function(_75,_76){return (!_75||this.model.mayHaveChildren(_75))?(_76?"dijitFolderOpened":"dijitFolderClosed"):"dijitLeaf";},getLabelClass:function(){},getRowClass:function(){},getIconStyle:function(){},getLabelStyle:function(){},getRowStyle:function(){},getTooltip:function(){return "";},_onKeyPress:function(e){if(e.altKey){return;}var _77=_12.getEnclosingWidget(e.target);if(!_77){return;}var key=e.charOrCode;if(typeof key=="string"&&key!=" "){if(!e.altKey&&!e.ctrlKey&&!e.shiftKey&&!e.metaKey){this._onLetterKeyNav({node:_77,key:key.toLowerCase()});_b.stop(e);}}else{if(this._curSearch){clearTimeout(this._curSearch.timer);delete this._curSearch;}var map=this._keyHandlerMap;if(!map){map={};map[_e.ENTER]="_onEnterKey";map[_e.SPACE]=map[" "]="_onEnterKey";map[this.isLeftToRight()?_e.LEFT_ARROW:_e.RIGHT_ARROW]="_onLeftArrow";map[this.isLeftToRight()?_e.RIGHT_ARROW:_e.LEFT_ARROW]="_onRightArrow";map[_e.UP_ARROW]="_onUpArrow";map[_e.DOWN_ARROW]="_onDownArrow";map[_e.HOME]="_onHomeKey";map[_e.END]="_onEndKey";this._keyHandlerMap=map;}if(this._keyHandlerMap[key]){this[this._keyHandlerMap[key]]({node:_77,item:_77.item,evt:e});_b.stop(e);}}},_onEnterKey:function(_78){this._publish("execute",{item:_78.item,node:_78.node});this.dndController.userSelect(_78.node,_2.isCopyKey(_78.evt),_78.evt.shiftKey);this.onClick(_78.item,_78.node,_78.evt);},_onDownArrow:function(_79){var _7a=this._getNextNode(_79.node);if(_7a&&_7a.isTreeNode){this.focusNode(_7a);}},_onUpArrow:function(_7b){var _7c=_7b.node;var _7d=_7c.getPreviousSibling();if(_7d){_7c=_7d;while(_7c.isExpandable&&_7c.isExpanded&&_7c.hasChildren()){var _7e=_7c.getChildren();_7c=_7e[_7e.length-1];}}else{var _7f=_7c.getParent();if(!(!this.showRoot&&_7f===this.rootNode)){_7c=_7f;}}if(_7c&&_7c.isTreeNode){this.focusNode(_7c);}},_onRightArrow:function(_80){var _81=_80.node;if(_81.isExpandable&&!_81.isExpanded){this._expandNode(_81);}else{if(_81.hasChildren()){_81=_81.getChildren()[0];if(_81&&_81.isTreeNode){this.focusNode(_81);}}}},_onLeftArrow:function(_82){var _83=_82.node;if(_83.isExpandable&&_83.isExpanded){this._collapseNode(_83);}else{var _84=_83.getParent();if(_84&&_84.isTreeNode&&!(!this.showRoot&&_84===this.rootNode)){this.focusNode(_84);}}},_onHomeKey:function(){var _85=this._getRootOrFirstNode();if(_85){this.focusNode(_85);}},_onEndKey:function(){var _86=this.rootNode;while(_86.isExpanded){var c=_86.getChildren();_86=c[c.length-1];}if(_86&&_86.isTreeNode){this.focusNode(_86);}},multiCharSearchDuration:250,_onLetterKeyNav:function(_87){var cs=this._curSearch;if(cs){cs.pattern=cs.pattern+_87.key;clearTimeout(cs.timer);}else{cs=this._curSearch={pattern:_87.key,startNode:_87.node};}var _88=this;cs.timer=setTimeout(function(){delete _88._curSearch;},this.multiCharSearchDuration);var _89=cs.startNode;do{_89=this._getNextNode(_89);if(!_89){_89=this._getRootOrFirstNode();}}while(_89!==cs.startNode&&(_89.label.toLowerCase().substr(0,cs.pattern.length)!=cs.pattern));if(_89&&_89.isTreeNode){if(_89!==cs.startNode){this.focusNode(_89);}}},isExpandoNode:function(_8a,_8b){return _7.isDescendant(_8a,_8b.expandoNode);},_onClick:function(_8c,e){var _8d=e.target,_8e=this.isExpandoNode(_8d,_8c);if((this.openOnClick&&_8c.isExpandable)||_8e){if(_8c.isExpandable){this._onExpandoClick({node:_8c});}}else{this._publish("execute",{item:_8c.item,node:_8c,evt:e});this.onClick(_8c.item,_8c,e);this.focusNode(_8c);}_b.stop(e);},_onDblClick:function(_8f,e){var _90=e.target,_91=(_90==_8f.expandoNode||_90==_8f.expandoNodeText);if((this.openOnDblClick&&_8f.isExpandable)||_91){if(_8f.isExpandable){this._onExpandoClick({node:_8f});}}else{this._publish("execute",{item:_8f.item,node:_8f,evt:e});this.onDblClick(_8f.item,_8f,e);this.focusNode(_8f);}_b.stop(e);},_onExpandoClick:function(_92){var _93=_92.node;this.focusNode(_93);if(_93.isExpanded){this._collapseNode(_93);}else{this._expandNode(_93);}},onClick:function(){},onDblClick:function(){},onOpen:function(){},onClose:function(){},_getNextNode:function(_94){if(_94.isExpandable&&_94.isExpanded&&_94.hasChildren()){return _94.getChildren()[0];}else{while(_94&&_94.isTreeNode){var _95=_94.getNextSibling();if(_95){return _95;}_94=_94.getParent();}return null;}},_getRootOrFirstNode:function(){return this.showRoot?this.rootNode:this.rootNode.getChildren()[0];},_collapseNode:function(_96){if(_96._expandNodeDeferred){delete _96._expandNodeDeferred;}if(_96.state=="LOADING"){return;}if(_96.isExpanded){var ret=_96.collapse();this.onClose(_96.item,_96);this._state(_96,false);return ret;}},_expandNode:function(_97){var def=new _5();if(_97._expandNodeDeferred){return _97._expandNodeDeferred;}var _98=this.model,_99=_97.item,_9a=this;if(!_97._loadDeferred){_97.markProcessing();_97._loadDeferred=new _5();_98.getChildren(_99,function(_9b){_97.unmarkProcessing();_97.setChildItems(_9b).then(function(){_97._loadDeferred.resolve(_9b);});},function(err){console.error(_9a,": error loading "+_97.label+" children: ",err);_97._loadDeferred.reject(err);});}_97._loadDeferred.then(_f.hitch(this,function(){_97.expand().then(function(){def.resolve(true);});this.onOpen(_97.item,_97);this._state(_97,true);}));return def;},focusNode:function(_9c){_11.focus(_9c.labelNode);},_onNodeFocus:function(_9d){if(_9d&&_9d!=this.lastFocused){if(this.lastFocused&&!this.lastFocused._destroyed){this.lastFocused.setFocusable(false);}_9d.setFocusable(true);this.lastFocused=_9d;}},_onNodeMouseEnter:function(){},_onNodeMouseLeave:function(){},_onItemChange:function(_9e){var _9f=this.model,_a0=_9f.getIdentity(_9e),_a1=this._itemNodesMap[_a0];if(_a1){var _a2=this.getLabel(_9e),_a3=this.getTooltip(_9e);_1.forEach(_a1,function(_a4){_a4.set({item:_9e,label:_a2,tooltip:_a3});_a4._updateItemClasses(_9e);});}},_onItemChildrenChange:function(_a5,_a6){var _a7=this.model,_a8=_a7.getIdentity(_a5),_a9=this._itemNodesMap[_a8];if(_a9){_1.forEach(_a9,function(_aa){_aa.setChildItems(_a6);});}},_onItemDelete:function(_ab){var _ac=this.model,_ad=_ac.getIdentity(_ab),_ae=this._itemNodesMap[_ad];if(_ae){_1.forEach(_ae,function(_af){this.dndController.removeTreeNode(_af);var _b0=_af.getParent();if(_b0){_b0.removeChild(_af);}_af.destroyRecursive();},this);delete this._itemNodesMap[_ad];}},_initState:function(){this._openedNodes={};if(this.persist&&this.cookieName){var _b1=_3(this.cookieName);if(_b1){_1.forEach(_b1.split(","),function(_b2){this._openedNodes[_b2]=true;},this);}}},_state:function(_b3,_b4){if(!this.persist){return false;}var _b5=_1.map(_b3.getTreePath(),function(_b6){return this.model.getIdentity(_b6);},this).join("/");if(arguments.length===1){return this._openedNodes[_b5];}else{if(_b4){this._openedNodes[_b5]=true;}else{delete this._openedNodes[_b5];}var ary=[];for(var id in this._openedNodes){ary.push(id);}_3(this.cookieName,ary.join(","),{expires:365});}},destroy:function(){if(this._curSearch){clearTimeout(this._curSearch.timer);delete this._curSearch;}if(this.rootNode){this.rootNode.destroyRecursive();}if(this.dndController&&!_f.isString(this.dndController)){this.dndController.destroy();}this.rootNode=null;this.inherited(arguments);},destroyRecursive:function(){this.destroy();},resize:function(_b7){if(_b7){_9.setMarginBox(this.domNode,_b7);}this._nodePixelIndent=_9.position(this.tree.indentDetector).w;if(this.tree.rootNode){this.tree.rootNode.set("indent",this.showRoot?0:-1);}},_createTreeNode:function(_b8){return new _1e(_b8);},_setTextDirAttr:function(_b9){if(_b9&&this.textDir!=_b9){this._set("textDir",_b9);this.rootNode.set("textDir",_b9);}}});_46._TreeNode=_1e;return _46;});