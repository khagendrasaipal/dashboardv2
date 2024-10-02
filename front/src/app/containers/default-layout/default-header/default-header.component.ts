import { Component, Input, OnInit, TemplateRef } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { LoginService } from '../../../login/login.service';
import { ClassToggleService, HeaderComponent } from '@coreui/angular';
import { Router } from '@angular/router';
import { AppConfig } from '../../../app.config';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { ToastrService } from 'ngx-toastr';
import { DboardService } from '../../../dboard/dboard.service';

@Component({
  selector: 'app-default-header',
  templateUrl: './default-header.component.html',
})
export class DefaultHeaderComponent extends HeaderComponent implements OnInit {

  @Input() sidebarId: string = "sidebar";

  public newMessages = new Array(4)
  public newTasks = new Array(5)
  public newNotifications = new Array(5)
  orgs:any;
  mainorgid:any;
  baseUrl: string = AppConfig.baseUrl;
  dashurl= this.baseUrl+'dashboard?orgid=';
  weburl= this.baseUrl+'web?orgid=';
 
  modalRef?: BsModalRef;
  changePasswordFormLayout: any;
  changePasswordForm: any;
  model: any;
  usertype:any;
  orglist:any;
  // @Input() mainorgid:any;
  
  

  constructor(private ts: ToastrService,private classToggler: ClassToggleService,private RS:DboardService,private loginService: LoginService,private modalService: BsModalService, private router: Router,private fb: FormBuilder) {
    super();
    this.mainorgid=this.getorginfo();
    this.changePasswordFormLayout = {
      opassword: [null, [Validators.required]],
      password: [null, [Validators.required, Validators.pattern('^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$')]],
      rpassword: [null, [Validators.required, Validators.pattern('^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$')]]
    }

    this.changePasswordForm = fb.group(this.changePasswordFormLayout)
  }
  logout() {
    this.loginService.removeUserData();
    this.router.navigate(['/dashboard']);
}
ngOnInit(): void {
  // this.open=false;
  // this.getorginfo();
  // this.orgs=this.loginService.getuserinfo();
  // console.log(this.orgs);
  this.getUserinfo();
  this.getorgnization();
  
}

openModal(template: TemplateRef<any>) {
  this.modalRef = this.modalService.show(template,
    Object.assign({}, { class: 'gray modal-lg' }));
}
open=false;
getUserinfo(){

  this.loginService.getuserinfo().subscribe({
    next: (result:any) => {
    this.open=true;
        // console.log(result.data[0]);
        this.usertype=result.data[0].usertype;
     },
     error : err => {
    this.open=false;
      //  this.toastr.error(err.error, 'Error');
     }
     
   }
   );
  // console.log(this.loginService.getuserinfo());
  // this.orgs=this.loginService.getuserinfo();
}

setOrg(id:any){
  this.mainorgid=id+"&admin=1";
}

getorgnization(){
  // alert(this.usertype);
  this.loginService.getorganization().subscribe({
    next: (result:any) => {
        // console.log(result.data[0]);
        this.orglist=result.data;
     },
     error : err => {
      //  this.toastr.error(err.error, 'Error');
     }
     
   }
   );
}

showPassword(e: any){
  const el: any = e.target;
  const element = el.parentElement?.parentElement.previousElementSibling?.firstChild
  if (element.type == "text"){
    el.classList.remove('bi-eye-fill');
    el.classList.add('bi-eye-slash-fill')
    element.type = "password";
   
  }  
  else{
    element.type = "text";
    el.classList.remove('bi-eye-slash-fill')
    el.classList.add('bi-eye-fill');
    
  }
}

userInfo:any

changePassword(){
  if (this.changePasswordForm.valid) {
  if (this.changePasswordForm.value.password == this.changePasswordForm.value.rpassword && (this.changePasswordForm.value.password!==null && this.changePasswordForm.value.rpassword!==null)){
    this.model = this.changePasswordForm.value;
    this.updatePassword(this.loginService.retriveUserData().user.userid);
  }
  else{
    this.ts.error("Passwords do not match","Error")
  }
}else{
  Object.keys(this.changePasswordForm.controls).forEach(field => {
    const singleFormControl = this.changePasswordForm.get(field);
    singleFormControl?.markAsTouched({onlySelf: true});
  });
}
}

updatePassword(id: any){
  let upd = this.model;
  this.RS.updateSelf(id, upd).subscribe(result => {
    this.ts.success('Item Successfully Updated!', 'Success');
    this.changePasswordForm =this.fb.group(this.changePasswordFormLayout);
    this.modalRef?.hide()
    // this.getList();
  }, error => {
    this.ts.error(error.error.message, 'Error');
  });
}
getorginfo(){
this.loginService.getuserinfo().subscribe({
  next: (result:any) => {
    // console.log(result);
       this.orgs = result.data[0].orgname; 
       this.mainorgid = result.data[0].orgid; 
   },
   error : err => {
    //  console.log("error");
   }
   
 }
 );
}
}

