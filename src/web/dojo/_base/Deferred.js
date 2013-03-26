/*
	Copyright (c) 2004-2011, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/

//>>built
define("dojo/_base/Deferred",["./kernel","./config","./lang"],function(_1,_2,_3){var _4=function(){};var _5=Object.freeze||function(){};_1.Deferred=function(_6){var _7,_8,_9,_a,_b;var _c=(this.promise={});function _d(_e){if(_8){throw new Error("This deferred has already been resolved");}_7=_e;_8=true;_f();};function _f(){var _10;while(!_10&&_b){var _11=_b;_b=_b.next;if((_10=(_11.progress==_4))){_8=false;}var _12=(_9?_11.error:_11.resolved);if(_12){try{var _13=_12(_7);if(_13&&typeof _13.then==="function"){_13.then(_3.hitch(_11.deferred,"resolve"),_3.hitch(_11.deferred,"reject"),_3.hitch(_11.deferred,"progress"));continue;}var _14=_10&&_13===undefined;if(_10&&!_14){_9=_13 instanceof Error;}_11.deferred[_14&&_9?"reject":"resolve"](_14?_7:_13);}catch(e){_11.deferred.reject(e);}}else{if(_9){_11.deferred.reject(_7);}else{_11.deferred.resolve(_7);}}}};this.resolve=this.callback=function(_15){this.fired=0;this.results=[_15,null];_d(_15);};this.reject=this.errback=function(_16){_9=true;this.fired=1;_d(_16);this.results=[null,_16];if(!_16||_16.log!==false){(_2.deferredOnError||function(x){console.error(x);})(_16);}};this.progress=function(_17){var _18=_b;while(_18){var _19=_18.progress;_19&&_19(_17);_18=_18.next;}};this.addCallbacks=function(_1a,_1b){this.then(_1a,_1b,_4);return this;};_c.then=this.then=function(_1c,_1d,_1e){var _1f=_1e==_4?this:new _1.Deferred(_c.cancel);var _20={resolved:_1c,error:_1d,progress:_1e,deferred:_1f};if(_b){_a=_a.next=_20;}else{_b=_a=_20;}if(_8){_f();}return _1f.promise;};var _21=this;_c.cancel=this.cancel=function(){if(!_8){var _22=_6&&_6(_21);if(!_8){if(!(_22 instanceof Error)){_22=new Error(_22);}_22.log=false;_21.reject(_22);}}};_5(_c);};_3.extend(_1.Deferred,{addCallback:function(_23){return this.addCallbacks(_3.hitch.apply(_1,arguments));},addErrback:function(_24){return this.addCallbacks(null,_3.hitch.apply(_1,arguments));},addBoth:function(_25){var _26=_3.hitch.apply(_1,arguments);return this.addCallbacks(_26,_26);},fired:-1});_1.Deferred.when=_1.when=function(_27,_28,_29,_2a){if(_27&&typeof _27.then==="function"){return _27.then(_28,_29,_2a);}return _28?_28(_27):_27;};return _1.Deferred;});