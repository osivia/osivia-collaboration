/*
@license
dhtmlxScheduler v.5.1.0 Stardard

This software is covered by GPL license. You also can obtain Commercial or Enterprise license to use it in non-GPL project - please contact sales@dhtmlx.com. Usage without proper license is prohibited.

(c) Dinamenta, UAB.
*/
scheduler.config.limit_start=null,scheduler.config.limit_end=null,scheduler.config.limit_view=!1,scheduler.config.check_limits=!0,scheduler.config.mark_now=!0,scheduler.config.display_marked_timespans=!0,scheduler._temp_limit_scope=function(){function e(e,t,a,i,r){var n=scheduler,d=[],l={_props:"map_to",matrix:"y_property"};for(var o in l){var s=l[o];if(n[o])for(var _ in n[o]){var c=n[o][_],u=c[s];e[u]&&(d=n._add_timespan_zones(d,scheduler._get_blocked_zones(t[_],e[u],a,i,r)))}}return d=n._add_timespan_zones(d,scheduler._get_blocked_zones(t,"global",a,i,r));
}var t=null,a="dhx_time_block",i="default",r=function(e,t,a){return t instanceof Date&&a instanceof Date?(e.start_date=t,e.end_date=a):(e.days=t,e.zones=a),e},n=function(e,t,i){var n="object"==typeof e?e:{days:e};return n.type=a,n.css="",t&&(i&&(n.sections=i),n=r(n,e,t)),n};scheduler.blockTime=function(e,t,a){var i=n(e,t,a);return scheduler.addMarkedTimespan(i)},scheduler.unblockTime=function(e,t,a){t=t||"fullday";var i=n(e,t,a);return scheduler.deleteMarkedTimespan(i)},scheduler.attachEvent("onBeforeViewChange",function(e,t,a,i){
function r(e,t){var a=scheduler.config.limit_start,i=scheduler.config.limit_end,r=scheduler.date.add(e,1,t);return e.valueOf()>i.valueOf()||r<=a.valueOf()}return scheduler.config.limit_view&&(i=i||t,a=a||e,r(i,a)&&t.valueOf()!=i.valueOf())?(setTimeout(function(){var e=r(t,a)?scheduler.config.limit_start:t;scheduler.setCurrentView(r(e,a)?null:e,a)},1),!1):!0}),scheduler.checkInMarkedTimespan=function(t,a,r){a=a||i;for(var n=!0,d=new Date(t.start_date.valueOf()),l=scheduler.date.add(d,1,"day"),o=scheduler._marked_timespans;d<t.end_date;d=scheduler.date.date_part(l),
l=scheduler.date.add(d,1,"day")){var s=+scheduler.date.date_part(new Date(d)),_=d.getDay(),c=e(t,o,_,s,a);if(c)for(var u=0;u<c.length;u+=2){var h=scheduler._get_zone_minutes(d),p=t.end_date>l||t.end_date.getDate()!=d.getDate()?1440:scheduler._get_zone_minutes(t.end_date),v=c[u],m=c[u+1];if(p>v&&m>h&&(n="function"==typeof r?r(t,h,p,v,m):!1,!n))break}}return!n};var d=scheduler.checkLimitViolation=function(e){if(!e)return!0;if(!scheduler.config.check_limits)return!0;var t=scheduler,i=t.config,r=[];if(e.rec_type)for(var n=scheduler.getRecDates(e),d=0;d<n.length;d++){
var l=scheduler._copy_event(e);scheduler._lame_copy(l,n[d]),r.push(l)}else r=[e];for(var o=!0,s=0;s<r.length;s++){var _=!0,l=r[s];l._timed=scheduler.isOneDayEvent(l),_=i.limit_start&&i.limit_end?l.start_date.valueOf()>=i.limit_start.valueOf()&&l.end_date.valueOf()<=i.limit_end.valueOf():!0,_&&(_=!scheduler.checkInMarkedTimespan(l,a,function(e,a,i,r,n){var d=!0;return n>=a&&a>=r&&((1440==n||n>i)&&(d=!1),e._timed&&t._drag_id&&"new-size"==t._drag_mode?(e.start_date.setHours(0),e.start_date.setMinutes(n)):d=!1),
(i>=r&&n>i||r>a&&i>n)&&(e._timed&&t._drag_id&&"new-size"==t._drag_mode?(e.end_date.setHours(0),e.end_date.setMinutes(r)):d=!1),d})),_||(_=t.checkEvent("onLimitViolation")?t.callEvent("onLimitViolation",[l.id,l]):_),o=o&&_}return o||(t._drag_id=null,t._drag_mode=null),o};scheduler._get_blocked_zones=function(e,t,a,i,r){var n=[];if(e&&e[t])for(var d=e[t],l=this._get_relevant_blocked_zones(a,i,d,r),o=0;o<l.length;o++)n=this._add_timespan_zones(n,l[o].zones);return n},scheduler._get_relevant_blocked_zones=function(e,t,a,i){
var r=a[t]&&a[t][i]?a[t][i]:a[e]&&a[e][i]?a[e][i]:[];return r},scheduler.attachEvent("onMouseDown",function(e){return!(e==a)}),scheduler.attachEvent("onBeforeDrag",function(e){return e?d(scheduler.getEvent(e)):!0}),scheduler.attachEvent("onClick",function(e,t){return d(scheduler.getEvent(e))}),scheduler.attachEvent("onBeforeLightbox",function(e){var a=scheduler.getEvent(e);return t=[a.start_date,a.end_date],d(a)}),scheduler.attachEvent("onEventSave",function(e,t,a){if(!t.start_date||!t.end_date){
var i=scheduler.getEvent(e);t.start_date=new Date(i.start_date),t.end_date=new Date(i.end_date)}if(t.rec_type){var r=scheduler._lame_clone(t);return scheduler._roll_back_dates(r),d(r)}return d(t)}),scheduler.attachEvent("onEventAdded",function(e){if(!e)return!0;var t=scheduler.getEvent(e);return!d(t)&&scheduler.config.limit_start&&scheduler.config.limit_end&&(t.start_date<scheduler.config.limit_start&&(t.start_date=new Date(scheduler.config.limit_start)),t.start_date.valueOf()>=scheduler.config.limit_end.valueOf()&&(t.start_date=this.date.add(scheduler.config.limit_end,-1,"day")),
t.end_date<scheduler.config.limit_start&&(t.end_date=new Date(scheduler.config.limit_start)),t.end_date.valueOf()>=scheduler.config.limit_end.valueOf()&&(t.end_date=this.date.add(scheduler.config.limit_end,-1,"day")),t.start_date.valueOf()>=t.end_date.valueOf()&&(t.end_date=this.date.add(t.start_date,this.config.event_duration||this.config.time_step,"minute")),t._timed=this.isOneDayEvent(t)),!0}),scheduler.attachEvent("onEventChanged",function(e){if(!e)return!0;var a=scheduler.getEvent(e);if(!d(a)){
if(!t)return!1;a.start_date=t[0],a.end_date=t[1],a._timed=this.isOneDayEvent(a)}return!0}),scheduler.attachEvent("onBeforeEventChanged",function(e,t,a){return d(e)}),scheduler.attachEvent("onBeforeEventCreated",function(e){var t=scheduler.getActionData(e).date,a={_timed:!0,start_date:t,end_date:scheduler.date.add(t,scheduler.config.time_step,"minute")};return d(a)}),scheduler.attachEvent("onViewChange",function(){scheduler._mark_now()}),scheduler.attachEvent("onSchedulerResize",function(){return window.setTimeout(function(){
scheduler._mark_now()},1),!0}),scheduler.attachEvent("onAfterSchedulerResize",function(){return window.setTimeout(function(){scheduler._mark_now()},1),!0}),scheduler.attachEvent("onTemplatesReady",function(){scheduler._mark_now_timer=window.setInterval(function(){scheduler._is_initialized()&&scheduler._mark_now()},6e4)}),scheduler._mark_now=function(e){var t="dhx_now_time";this._els[t]||(this._els[t]=[]);var a=scheduler._currentDate(),i=this.config;if(scheduler._remove_mark_now(),!e&&i.mark_now&&a<this._max_date&&a>this._min_date&&a.getHours()>=i.first_hour&&a.getHours()<i.last_hour){
var r=this.locate_holder_day(a);this._els[t]=scheduler._append_mark_now(r,a)}},scheduler._append_mark_now=function(e,t){var a="dhx_now_time",i=scheduler._get_zone_minutes(t),r={zones:[i,i+1],css:a,type:a};if(!this._table_view){if(this._props&&this._props[this._mode]){var n,d,l=this._props[this._mode],o=l.size||l.options.length;l.days>1?(l.size&&l.options.length&&(e=(l.position+e)/l.options.length*l.size),n=e,d=e+o):(n=0,d=n+o);for(var s=[],_=n;d>_;_++){var c=_;r.days=c;var u=scheduler._render_marked_timespan(r,null,c)[0];
s.push(u)}return s}return r.days=e,scheduler._render_marked_timespan(r,null,e)}return"month"==this._mode?(r.days=+scheduler.date.date_part(t),scheduler._render_marked_timespan(r,null,null)):void 0},scheduler._remove_mark_now=function(){for(var e="dhx_now_time",t=this._els[e],a=0;a<t.length;a++){var i=t[a],r=i.parentNode;r&&r.removeChild(i)}this._els[e]=[]},scheduler._marked_timespans={global:{}},scheduler._get_zone_minutes=function(e){return 60*e.getHours()+e.getMinutes()},scheduler._prepare_timespan_options=function(e){
var t=[],a=[];if("fullweek"==e.days&&(e.days=[0,1,2,3,4,5,6]),e.days instanceof Array){for(var r=e.days.slice(),n=0;n<r.length;n++){var d=scheduler._lame_clone(e);d.days=r[n],t.push.apply(t,scheduler._prepare_timespan_options(d))}return t}if(!e||!(e.start_date&&e.end_date&&e.end_date>e.start_date||void 0!==e.days&&e.zones)&&!e.type)return t;var l=0,o=1440;"fullday"==e.zones&&(e.zones=[l,o]),e.zones&&e.invert_zones&&(e.zones=scheduler.invertZones(e.zones)),e.id=scheduler.uid(),e.css=e.css||"",e.type=e.type||i;
var s=e.sections;if(s){for(var _ in s)if(s.hasOwnProperty(_)){var c=s[_];c instanceof Array||(c=[c]);for(var n=0;n<c.length;n++){var u=scheduler._lame_copy({},e);u.sections={},u.sections[_]=c[n],a.push(u)}}}else a.push(e);for(var h=0;h<a.length;h++){var p=a[h],v=p.start_date,m=p.end_date;if(v&&m)for(var g=scheduler.date.date_part(new Date(v)),b=scheduler.date.add(g,1,"day");m>g;){var u=scheduler._lame_copy({},p);delete u.start_date,delete u.end_date,u.days=g.valueOf();var f=v>g?scheduler._get_zone_minutes(v):l,y=m>b||m.getDate()!=g.getDate()?o:scheduler._get_zone_minutes(m);
u.zones=[f,y],t.push(u),g=b,b=scheduler.date.add(b,1,"day")}else p.days instanceof Date&&(p.days=scheduler.date.date_part(p.days).valueOf()),p.zones=e.zones.slice(),t.push(p)}return t},scheduler._get_dates_by_index=function(e,t,a){var i=[];t=scheduler.date.date_part(new Date(t||scheduler._min_date)),a=new Date(a||scheduler._max_date);for(var r=t.getDay(),n=e-r>=0?e-r:7-t.getDay()+e,d=scheduler.date.add(t,n,"day");a>d;d=scheduler.date.add(d,1,"week"))i.push(d);return i},scheduler._get_css_classes_by_config=function(e){
var t=[];return e.type==a&&(t.push(a),e.css&&t.push(a+"_reset")),t.push("dhx_marked_timespan",e.css),t.join(" ")},scheduler._get_block_by_config=function(e){var t=document.createElement("div");return e.html&&("string"==typeof e.html?t.innerHTML=e.html:t.appendChild(e.html)),t},scheduler._render_marked_timespan=function(e,t,a){var i=[],r=scheduler.config,n=this._min_date,d=this._max_date,l=!1;if(!r.display_marked_timespans)return i;if(!a&&0!==a){if(e.days<7)a=e.days;else{var o=new Date(e.days);if(l=+o,
!(+d>+o&&+o>=+n))return i;a=o.getDay()}var s=n.getDay();s>a?a=7-(s-a):a-=s}var _=e.zones,c=scheduler._get_css_classes_by_config(e);if(scheduler._table_view&&"month"==scheduler._mode){var u=[],h=[];if(t)u.push(t),h.push(a);else{h=l?[l]:scheduler._get_dates_by_index(a);for(var p=0;p<h.length;p++)u.push(this._scales[h[p]])}for(var p=0;p<u.length;p++){t=u[p],a=h[p];var v=Math.floor((this._correct_shift(a,1)-n.valueOf())/(864e5*this._cols.length)),m=this.locate_holder_day(a,!1)%this._cols.length;if(!this._ignores[m]){
var g=scheduler._get_block_by_config(e),b=Math.max(t.offsetHeight-1,0),f=Math.max(t.offsetWidth-1,0),y=this._colsS[m],x=this._colsS.heights[v]+(this._colsS.height?this.xy.month_scale_height+2:2)-1;g.className=c,g.style.top=x+"px",g.style.lineHeight=g.style.height=b+"px";for(var k=0;k<_.length;k+=2){var w=_[p],D=_[p+1];if(w>=D)return[];var N=g.cloneNode(!0);N.style.left=y+Math.round(w/1440*f)+"px",N.style.width=Math.round((D-w)/1440*f)+"px",t.appendChild(N),i.push(N)}}}}else{var M=a;if(this._ignores[this.locate_holder_day(a,!1)])return i;
if(this._props&&this._props[this._mode]&&e.sections&&e.sections[this._mode]){var E=this._props[this._mode];M=E.order[e.sections[this._mode]];var S=E.order[e.sections[this._mode]];if(E.days>1){var A=E.size||E.options.length;M=M*A+S}else M=S,E.size&&M>E.position+E.size&&(M=0)}t=t?t:scheduler.locate_holder(M);for(var p=0;p<_.length;p+=2){var w=Math.max(_[p],60*r.first_hour),D=Math.min(_[p+1],60*r.last_hour);if(w>=D){if(p+2<_.length)continue;return[]}var N=scheduler._get_block_by_config(e);N.className=c;
var O=24*this.config.hour_size_px+1,T=36e5;N.style.top=Math.round((60*w*1e3-this.config.first_hour*T)*this.config.hour_size_px/T)%O+"px",N.style.lineHeight=N.style.height=Math.max(Math.round(60*(D-w)*1e3*this.config.hour_size_px/T)%O,1)+"px",t.appendChild(N),i.push(N)}}return i},scheduler._mark_timespans=function(){var e=this._els.dhx_cal_data[0],t=[];if(scheduler._table_view&&"month"==scheduler._mode)for(var a in this._scales){var i=new Date(+a);t.push.apply(t,scheduler._on_scale_add_marker(this._scales[a],i));
}else for(var i=new Date(scheduler._min_date),r=0,n=e.childNodes.length;n>r;r++){var d=e.childNodes[r];d.firstChild&&scheduler._getClassName(d.firstChild).indexOf("dhx_scale_hour")>-1||(t.push.apply(t,scheduler._on_scale_add_marker(d,i)),i=scheduler.date.add(i,1,"day"))}return t},scheduler.markTimespan=function(e){var t=!1;this._els.dhx_cal_data||(scheduler.get_elements(),t=!0);var a=scheduler._marked_timespans_ids,i=scheduler._marked_timespans_types,r=scheduler._marked_timespans;scheduler.deleteMarkedTimespan(),
scheduler.addMarkedTimespan(e);var n=scheduler._mark_timespans();return t&&(scheduler._els=[]),scheduler._marked_timespans_ids=a,scheduler._marked_timespans_types=i,scheduler._marked_timespans=r,n},scheduler.unmarkTimespan=function(e){if(e)for(var t=0;t<e.length;t++){var a=e[t];a.parentNode&&a.parentNode.removeChild(a)}},scheduler._addMarkerTimespanConfig=function(e){var t="global",a=scheduler._marked_timespans,i=e.id,r=scheduler._marked_timespans_ids;r[i]||(r[i]=[]);var n=e.days,d=e.sections,l=e.type;
if(e.id=i,d){for(var o in d)if(d.hasOwnProperty(o)){a[o]||(a[o]={});var s=d[o],_=a[o];_[s]||(_[s]={}),_[s][n]||(_[s][n]={}),_[s][n][l]||(_[s][n][l]=[],scheduler._marked_timespans_types||(scheduler._marked_timespans_types={}),scheduler._marked_timespans_types[l]||(scheduler._marked_timespans_types[l]=!0));var c=_[s][n][l];e._array=c,c.push(e),r[i].push(e)}}else{a[t][n]||(a[t][n]={}),a[t][n][l]||(a[t][n][l]=[]),scheduler._marked_timespans_types||(scheduler._marked_timespans_types={}),scheduler._marked_timespans_types[l]||(scheduler._marked_timespans_types[l]=!0);
var c=a[t][n][l];e._array=c,c.push(e),r[i].push(e)}},scheduler._marked_timespans_ids={},scheduler.addMarkedTimespan=function(e){var t=scheduler._prepare_timespan_options(e);if(t.length){for(var a=t[0].id,i=0;i<t.length;i++)scheduler._addMarkerTimespanConfig(t[i]);return a}},scheduler._add_timespan_zones=function(e,t){var a=e.slice();if(t=t.slice(),!a.length)return t;for(var i=0;i<a.length;i+=2)for(var r=a[i],n=a[i+1],d=i+2==a.length,l=0;l<t.length;l+=2){var o=t[l],s=t[l+1];if(s>n&&n>=o||r>o&&s>=r)a[i]=Math.min(r,o),
a[i+1]=Math.max(n,s),i-=2;else{if(!d)continue;var _=r>o?0:2;a.splice(i+_,0,o,s)}t.splice(l--,2);break}return a},scheduler._subtract_timespan_zones=function(e,t){for(var a=e.slice(),i=0;i<a.length;i+=2)for(var r=a[i],n=a[i+1],d=0;d<t.length;d+=2){var l=t[d],o=t[d+1];if(o>r&&n>l){var s=!1;r>=l&&o>=n&&a.splice(i,2),l>r&&(a.splice(i,2,r,l),s=!0),n>o&&a.splice(s?i+2:i,s?0:2,o,n),i-=2;break}}return a},scheduler.invertZones=function(e){return scheduler._subtract_timespan_zones([0,1440],e.slice())},scheduler._delete_marked_timespan_by_id=function(e){
var t=scheduler._marked_timespans_ids[e];if(t)for(var a=0;a<t.length;a++)for(var i=t[a],r=i._array,n=0;n<r.length;n++)if(r[n]==i){r.splice(n,1);break}},scheduler._delete_marked_timespan_by_config=function(e){var t,a=scheduler._marked_timespans,r=e.sections,n=e.days,d=e.type||i;if(r){for(var l in r)if(r.hasOwnProperty(l)&&a[l]){var o=r[l];a[l][o]&&(t=a[l][o])}}else t=a.global;if(t)if(void 0!==n)t[n]&&t[n][d]&&(scheduler._addMarkerTimespanConfig(e),scheduler._delete_marked_timespans_list(t[n][d],e));else for(var s in t)if(t[s][d]){
var _=scheduler._lame_clone(e);e.days=s,scheduler._addMarkerTimespanConfig(_),scheduler._delete_marked_timespans_list(t[s][d],e)}},scheduler._delete_marked_timespans_list=function(e,t){for(var a=0;a<e.length;a++){var i=e[a],r=scheduler._subtract_timespan_zones(i.zones,t.zones);if(r.length)i.zones=r;else{e.splice(a,1),a--;for(var n=scheduler._marked_timespans_ids[i.id],d=0;d<n.length;d++)if(n[d]==i){n.splice(d,1);break}}}},scheduler.deleteMarkedTimespan=function(e){if(arguments.length||(scheduler._marked_timespans={
global:{}},scheduler._marked_timespans_ids={},scheduler._marked_timespans_types={}),"object"!=typeof e)scheduler._delete_marked_timespan_by_id(e);else{e.start_date&&e.end_date||(void 0!==e.days||e.type||(e.days="fullweek"),e.zones||(e.zones="fullday"));var t=[];if(e.type)t.push(e.type);else for(var a in scheduler._marked_timespans_types)t.push(a);for(var i=scheduler._prepare_timespan_options(e),r=0;r<i.length;r++)for(var n=i[r],d=0;d<t.length;d++){var l=scheduler._lame_clone(n);l.type=t[d],scheduler._delete_marked_timespan_by_config(l);
}}},scheduler._get_types_to_render=function(e,t){var a=e?scheduler._lame_copy({},e):{};for(var i in t||{})t.hasOwnProperty(i)&&(a[i]=t[i]);return a},scheduler._get_configs_to_render=function(e){var t=[];for(var a in e)e.hasOwnProperty(a)&&t.push.apply(t,e[a]);return t},scheduler._on_scale_add_marker=function(e,t){if(!scheduler._table_view||"month"==scheduler._mode){var a=t.getDay(),i=t.valueOf(),r=this._mode,n=scheduler._marked_timespans,d=[],l=[];if(this._props&&this._props[r]){var o=this._props[r],s=o.options,_=scheduler._get_unit_index(o,t),c=s[_];
if(o.days>1){var u=864e5,h=Math.round((t-scheduler._min_date)/u),p=o.size||s.length;t=scheduler.date.add(scheduler._min_date,Math.floor(h/p),"day"),t=scheduler.date.date_part(t)}else t=scheduler.date.date_part(new Date(this._date));if(a=t.getDay(),i=t.valueOf(),n[r]&&n[r][c.key]){var v=n[r][c.key],m=scheduler._get_types_to_render(v[a],v[i]);d.push.apply(d,scheduler._get_configs_to_render(m))}}var g=n.global,b=g[i]||g[a];d.push.apply(d,scheduler._get_configs_to_render(b));for(var f=0;f<d.length;f++)l.push.apply(l,scheduler._render_marked_timespan(d[f],e,t));
return l}},scheduler.attachEvent("onScaleAdd",function(){scheduler._on_scale_add_marker.apply(scheduler,arguments)}),scheduler.dblclick_dhx_marked_timespan=function(e,t){scheduler.callEvent("onScaleDblClick",[scheduler.getActionData(e).date,t,e]),scheduler.config.dblclick_create&&scheduler.addEventNow(scheduler.getActionData(e).date,null,e)}},scheduler._temp_limit_scope();
//# sourceMappingURL=../sources/ext/dhtmlxscheduler_limit.js.map