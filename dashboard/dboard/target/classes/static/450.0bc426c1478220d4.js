"use strict";(self.webpackChunkcoreui_free_angular_admin_template=self.webpackChunkcoreui_free_angular_admin_template||[]).push([[450],{9082:(u,d,t)=>{t.r(d),t.d(d,{ChartsModule:()=>b});var g=t(9808),o=t(5033),n=t(5093),i=t(5366),a=t(5e3),h=t(2362);const m=[{path:"",component:(()=>{class r{constructor(){this.months=["January","February","March","April","May","June","July","August","September","October","November","December"],this.chartBarData={labels:[...this.months].slice(0,7),datasets:[{label:"GitHub Commits",backgroundColor:"#f87979",data:[40,20,12,39,17,42,79]}]},this.chartLineData={labels:[...this.months].slice(0,7),datasets:[{label:"My First dataset",backgroundColor:"rgba(220, 220, 220, 0.2)",borderColor:"rgba(220, 220, 220, 1)",pointBackgroundColor:"rgba(220, 220, 220, 1)",pointBorderColor:"#fff",data:[this.randomData,this.randomData,this.randomData,this.randomData,this.randomData,this.randomData,this.randomData]},{label:"My Second dataset",backgroundColor:"rgba(151, 187, 205, 0.2)",borderColor:"rgba(151, 187, 205, 1)",pointBackgroundColor:"rgba(151, 187, 205, 1)",pointBorderColor:"#fff",data:[this.randomData,this.randomData,this.randomData,this.randomData,this.randomData,this.randomData,this.randomData]}]},this.chartLineOptions={maintainAspectRatio:!1},this.chartDoughnutData={labels:["VueJs","EmberJs","ReactJs","Angular"],datasets:[{backgroundColor:["#41B883","#E46651","#00D8FF","#DD1B16"],data:[40,20,80,10]}]},this.chartPieData={labels:["Red","Green","Yellow"],datasets:[{data:[300,50,100],backgroundColor:["#FF6384","#36A2EB","#FFCE56"],hoverBackgroundColor:["#FF6384","#36A2EB","#FFCE56"]}]},this.chartPolarAreaData={labels:["Red","Green","Yellow","Grey","Blue"],datasets:[{data:[11,16,7,3,14],backgroundColor:["#FF6384","#4BC0C0","#FFCE56","#E7E9ED","#36A2EB"]}]},this.chartRadarData={labels:["Eating","Drinking","Sleeping","Designing","Coding","Cycling","Running"],datasets:[{label:"2020",backgroundColor:"rgba(179,181,198,0.2)",borderColor:"rgba(179,181,198,1)",pointBackgroundColor:"rgba(179,181,198,1)",pointBorderColor:"#fff",pointHoverBackgroundColor:"#fff",pointHoverBorderColor:"rgba(179,181,198,1)",tooltipLabelColor:"rgba(179,181,198,1)",data:[65,59,90,81,56,55,40]},{label:"2021",backgroundColor:"rgba(255,99,132,0.2)",borderColor:"rgba(255,99,132,1)",pointBackgroundColor:"rgba(255,99,132,1)",pointBorderColor:"#fff",pointHoverBackgroundColor:"#fff",pointHoverBorderColor:"rgba(255,99,132,1)",tooltipLabelColor:"rgba(255,99,132,1)",data:[this.randomData,this.randomData,this.randomData,this.randomData,this.randomData,this.randomData,this.randomData]}]}}get randomData(){return Math.round(100*Math.random())}}return r.\u0275fac=function(e){return new(e||r)},r.\u0275cmp=a.Xpm({type:r,selectors:[["app-charts"]],decls:43,vars:6,consts:[["xs","12"],["href","charts"],["xs","12","lg","6"],[1,"mb-4"],["type","bar",3,"data"],["type","line",3,"data"],["type","doughnut",3,"data"],["type","pie",3,"data"],["type","polarArea",3,"data"],["type","radar",3,"data"]],template:function(e,s){1&e&&(a.TgZ(0,"c-row")(1,"c-col",0)(2,"app-docs-callout",1),a._uU(3," Angular wrapper component for Chart.js 3.6, the most popular charting library. "),a._UZ(4,"br"),a.qZA()(),a.TgZ(5,"c-col",2)(6,"c-card",3)(7,"c-card-header"),a._uU(8," Bar Chart "),a.qZA(),a.TgZ(9,"c-card-body"),a._UZ(10,"c-chart",4),a.qZA()()(),a.TgZ(11,"c-col",2)(12,"c-card",3)(13,"c-card-header"),a._uU(14," Line Chart "),a.qZA(),a.TgZ(15,"c-card-body"),a._UZ(16,"c-chart",5),a.qZA()()()(),a.TgZ(17,"c-row")(18,"c-col",2)(19,"c-card",3)(20,"c-card-header"),a._uU(21," Doughnut Chart "),a.qZA(),a.TgZ(22,"c-card-body"),a._UZ(23,"c-chart",6),a.qZA()()(),a.TgZ(24,"c-col",2)(25,"c-card",3)(26,"c-card-header"),a._uU(27," Pie Chart "),a.qZA(),a.TgZ(28,"c-card-body"),a._UZ(29,"c-chart",7),a.qZA()()()(),a.TgZ(30,"c-row")(31,"c-col",2)(32,"c-card",3)(33,"c-card-header"),a._uU(34," Polar Area Chart "),a.qZA(),a.TgZ(35,"c-card-body"),a._UZ(36,"c-chart",8),a.qZA()()(),a.TgZ(37,"c-col",2)(38,"c-card",3)(39,"c-card-header"),a._uU(40," Radar Chart "),a.qZA(),a.TgZ(41,"c-card-body"),a._UZ(42,"c-chart",9),a.qZA()()()()),2&e&&(a.xp6(10),a.Q6J("data",s.chartBarData),a.xp6(6),a.Q6J("data",s.chartLineData),a.xp6(7),a.Q6J("data",s.chartDoughnutData),a.xp6(6),a.Q6J("data",s.chartPieData),a.xp6(7),a.Q6J("data",s.chartPolarAreaData),a.xp6(6),a.Q6J("data",s.chartRadarData))},directives:[o.iok,o.Yp0,h.G,o.AkF,o.nkx,o.yue,n.d],styles:[""]}),r})(),data:{title:"Charts"}}];let p=(()=>{class r{}return r.\u0275fac=function(e){return new(e||r)},r.\u0275mod=a.oAB({type:r}),r.\u0275inj=a.cJS({imports:[[i.Bz.forChild(m)],i.Bz]}),r})();var C=t(7642);let b=(()=>{class r{}return r.\u0275fac=function(e){return new(e||r)},r.\u0275mod=a.oAB({type:r}),r.\u0275inj=a.cJS({imports:[[g.ez,p,n.N,o.dTQ,o.zE6,o.TXv,C.E]]}),r})()},7642:(u,d,t)=>{t.d(d,{E:()=>h});var g=t(9808),o=t(5366),n=t(5033),i=t(1728),a=t(5e3);let h=(()=>{class c{}return c.\u0275fac=function(p){return new(p||c)},c.\u0275mod=a.oAB({type:c}),c.\u0275inj=a.cJS({imports:[[g.ez,n.dGk,i.QX,o.Bz,n.P4_,n.gzQ,n.dfc]]}),c})()},4147:u=>{u.exports=JSON.parse('{"name":"coreui-free-angular-admin-template","version":"4.0.0-alpha.4","config":{"coreui_library_short_version":"4.0","coreui_library_docs_url":"https://coreui.io/angular/docs/"},"scripts":{"ng":"ng","start":"ng serve","build":"ng build","watch":"ng build --watch --configuration development","test":"ng test"},"private":true,"dependencies":{"@angular-material-extensions/faq":"^3.0.4","@angular/animations":"~13.2.0","@angular/cdk":"~13.2.0","@angular/common":"~13.2.0","@angular/compiler":"~13.2.0","@angular/core":"~13.2.0","@angular/flex-layout":"^15.0.0-beta.42","@angular/forms":"~13.2.0","@angular/language-service":"~13.2.0","@angular/material":"^17.2.2","@angular/platform-browser":"~13.2.0","@angular/platform-browser-dynamic":"~13.2.0","@angular/router":"~13.2.0","@auth0/angular-jwt":"^5.0.2","@coreui/angular":"^4.0.0-alpha.9","@coreui/angular-chartjs":"^2.0.0-alpha.9","@coreui/chartjs":"^3.0.0","@coreui/coreui":"~4.0.5","@coreui/icons":"^2.1.0","@coreui/icons-angular":"^3.0.0-alpha.4","@coreui/utils":"^1.3.1","@kolkov/angular-editor":"^2.2.0-beta.0","@ng-select/ng-select":"^8.3.0","@tinymce/tinymce-angular":"^6.0.1","@types/ckeditor":"^4.9.10","angular-highcharts":"^13.0.1","bootstrap-icons":"^1.8.1","chart.js":"^3.6.1","highcharts":"^10.1.0","highcharts-angular":"^3.0.0","jquery":"^3.6.0","ng-toggle-button":"^1.3.0","ng2-ckeditor":"^1.3.6","ngx-bootstrap":"^8.0.0","ngx-editor":"^14.0.0","ngx-image-cropper":"^6.1.0","ngx-perfect-scrollbar":"^10.1.1","ngx-toastr":"^14.2.1","ngx-toggle-button":"^1.0.0-beta.1","rxjs":"~7.5.0","tinymce":"^5.0.16","tslib":"^2.3.0","zone.js":"~0.11.4"},"devDependencies":{"@angular-devkit/build-angular":"~13.2.0","@angular/cli":"~13.2.0","@angular/compiler-cli":"~13.2.0","@angular/localize":"~13.2.0","@types/jasmine":"~3.10.3","@types/jquery":"^3.5.14","@types/node":"^16.11.25","jasmine-core":"~3.10.0","karma":"~6.3.16","karma-chrome-launcher":"~3.1.0","karma-coverage":"~2.2.0","karma-jasmine":"~4.0.1","karma-jasmine-html-reporter":"~1.7.0","typescript":"~4.5.2"},"engines":{"node":"^12.20.0 || ^14.15.0 || >=16.10.0","npm":">= 6"}}')}}]);