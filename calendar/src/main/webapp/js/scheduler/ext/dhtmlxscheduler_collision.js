/*
@license
dhtmlxScheduler v.5.1.0 Stardard

This software is covered by GPL license. You also can obtain Commercial or Enterprise license to use it in non-GPL project - please contact sales@dhtmlx.com. Usage without proper license is prohibited.

(c) Dinamenta, UAB.
*/
!function(){function e(e){var a=scheduler._get_section_view();a&&e&&(t=scheduler.getEvent(e)[scheduler._get_section_property()])}var t,a;scheduler.config.collision_limit=1,scheduler.attachEvent("onBeforeDrag",function(t){return e(t),!0}),scheduler.attachEvent("onBeforeLightbox",function(t){var r=scheduler.getEvent(t);return a=[r.start_date,r.end_date],e(t),!0}),scheduler.attachEvent("onEventChanged",function(e){if(!e||!scheduler.getEvent(e))return!0;var t=scheduler.getEvent(e);if(!scheduler.checkCollision(t)){
if(!a)return!1;t.start_date=a[0],t.end_date=a[1],t._timed=this.isOneDayEvent(t)}return!0}),scheduler.attachEvent("onBeforeEventChanged",function(e,t,a){return scheduler.checkCollision(e)}),scheduler.attachEvent("onEventAdded",function(e,t){var a=scheduler.checkCollision(t);a||scheduler.deleteEvent(e)}),scheduler.attachEvent("onEventSave",function(e,t,a){if(t=scheduler._lame_clone(t),t.id=e,!t.start_date||!t.end_date){var r=scheduler.getEvent(e);t.start_date=new Date(r.start_date),t.end_date=new Date(r.end_date);
}return t.rec_type&&scheduler._roll_back_dates(t),scheduler.checkCollision(t)}),scheduler._check_sections_collision=function(e,t){var a=scheduler._get_section_property();return e[a]==t[a]&&e.id!=t.id?!0:!1},scheduler.checkCollision=function(e){var a=[],r=scheduler.config.collision_limit;if(e.rec_type)for(var i=scheduler.getRecDates(e),n=0;n<i.length;n++)for(var s=scheduler.getEvents(i[n].start_date,i[n].end_date),o=0;o<s.length;o++)(s[o].event_pid||s[o].id)!=e.id&&a.push(s[o]);else{a=scheduler.getEvents(e.start_date,e.end_date);
for(var d=0;d<a.length;d++){var l=a[d];if(l.id==e.id||l.event_length&&[l.event_pid,l.event_length].join("#")==e.id){a.splice(d,1);break}}}var _=scheduler._get_section_view(),c=scheduler._get_section_property(),h=!0;if(_){for(var u=0,d=0;d<a.length;d++)a[d].id!=e.id&&this._check_sections_collision(a[d],e)&&u++;u>=r&&(h=!1)}else a.length>=r&&(h=!1);if(!h){var g=!scheduler.callEvent("onEventCollision",[e,a]);return g||(e[c]=t||e[c]),g}return h}}();
//# sourceMappingURL=../sources/ext/dhtmlxscheduler_collision.js.map