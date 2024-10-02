import { Component, OnInit } from '@angular/core';
import { LoginService } from 'src/app/login/login.service';
import { navItems } from './_navopen';

@Component({
  selector: 'app-dashboard-open',
  templateUrl: './layout-open.component.html',
})
export class DefaultLayoutOpenComponent implements OnInit{

  public navItems = navItems;
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
    this.loginService.removeUserData();
    this.getMenu();
  }
  getMenu(){
    var navv=[];
    // console.log(navItems);
    for (let i = 0; i < navItems.length; i++) {
    //  console.log(navItems[i]);
      if(navItems[i].name=="Setup"){
        var child=navItems[i].children||[];
        var p=navItems[i].children?.length||0;
        for(let k=0;k<p;k++){
          // console.log(child[k].name);
          if(child[k].name=="Organization"||child[k].name=="User"||child[k].name=="Category"||child[k].name=="Indicators"){
            if(this.loginService.retriveUserData().user.orgid==100){
              navv.push(navItems[i]);
            }else{
              child.splice(k, 1); 
            }
          }
        }
      }else{
        navv.push(navItems[i]);
      }
    }
    this.navs=navv;
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
