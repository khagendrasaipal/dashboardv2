import { Component, OnInit } from '@angular/core';
import { LoginService } from 'src/app/login/login.service';
import { navItems, navItems1 } from './_nav';

@Component({
  selector: 'app-dashboard',
  templateUrl: './default-layout.component.html',
})
export class DefaultLayoutComponent implements OnInit{

  public navItems = navItems;
  public navItems1 = navItems1;
  navs:any;
  public perfectScrollbarConfig = {
    suppressScrollX: true,
  };
  orgs:any;

  constructor(private loginService: LoginService) {
    // super();
    this.orgs=this.getorginfo();
    // console.log(loginService.retriveUserData().user.orgid);
  }
  ngOnInit(): void {
    // this.getorginfo();
    this.getMenu();
  }
  getMenu(){
    var navv=[];
    // console.log(navItems1);
    // console.log(navItems);
console.log(this.loginService.retriveUserData().user.orgid);
    if(this.loginService.retriveUserData().user.orgid!=100){
      console.log("hello user")
      for (let i = 0; i < navItems1.length; i++) {
        // if(navItems1[i].name=="Setup"){
        //   navv.push(navItems1[i]);
        // }else{
        // navv.push(navItems1[i]);
        // }
        console.log(navItems1[i]);
      navv.push(navItems1[i]);
      }
    }else{
      console.log("hello superadmin")
      for (let i = 0; i < navItems.length; i++) {
        navv.push(navItems[i]);
        }
        
    }
    this.navs=navv;
    // for (let i = 0; i < navItems.length; i++) {
    //   if(navItems[i].name=="Setup"){
    //     var child=navItems[i].children||[];
    //     var p=navItems[i].children?.length||0;
    //     for(let k=0;k<p;k++){
    //       if(child[k].name=="Organization"||child[k].name=="User"||child[k].name=="Category"||child[k].name=="Indicators"||child[k].name=="Faq Setup"){
    //         if(this.loginService.retriveUserData().user.orgid==100){
    //           navv.push(navItems[i]);
    //         }
    //         if(this.loginService.retriveUserData().user.orgid!=100){
    //           child.splice(k, 1); 
    //         }
    //       }
    //     }
    //   }else{
    //     navv.push(navItems[i]);
    //   }
    // }
    // this.navs=navv;
  }

  toggle(e: any){
    var element: HTMLElement = e.target.parentElement.parentElement.parentElement;
    var elementt: HTMLElement = e.target.parentElement.parentElement;
    var ele: HTMLElement = e.target.parentElement.parentElement.parentElement.parentElement;
    // console.log(element);
    element.classList.add('show');
    elementt.classList.add('show');
    ele.classList.add('show');
  }
  getorginfo(){
    this.loginService.getuserinfo().subscribe({
      next: (result:any) => {
        this.orgs=result.data[0].orgname;
           return result.data[0].orgname; 
         
           
       },
       error : err => {
        //  console.log("error");
       }
       
     }
     );
    }
}
