import { Component, OnInit } from '@angular/core';
import {FormGroup, FormControl, Validators, FormBuilder} from '@angular/forms'
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LoginService } from './login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login1.component.html',
  styleUrls: ['./login1.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  formLayout: any;
  orglist:any;
  constructor(private ls: LoginService, private router:Router, private toastr: ToastrService, private fb: FormBuilder,private route: ActivatedRoute) {
    
    this.formLayout = {
      username: ['', Validators.required],
      password: ['', Validators.required],
      // utype: ['0', Validators.required]
    };
    
    this.loginForm = fb.group(this.formLayout);
  }
type:any;
lg=true;
pp=true;
  ngOnInit(): void {
    this.route.queryParams.subscribe((params: { [x: string]: any; }) => {
      this.type = params['type'];
      if(this.type=="l"){
        this.lg=true;
        this.pp=false;
      }
      // else{
      //   this.lg=false;
      // }
      if(this.type=="p"){
        this.pp=true;
        this.lg=false;
      }
      // else{
      //   this.pp=false;
      // }
    });
    this.getOrglist();
  }
  getOrglist(){
    this.ls.getorganizationList().subscribe({
      next: (result:any) => {
          // console.log(result.data[0]);
          this.orglist=result.data;
          console.log(this.orglist);
       },
       error : err => {
        //  this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }

  submitForm(){
    // alert("hello");
    if(this.loginForm.valid){
      this.ls.login(this.loginForm.value).subscribe({
      next: (result:any) => {
        this.ls.storeUserData(result.data);
        this.router.navigate(['/dboard']);
       
       },
       error : err => {
         alert("Invalid Username or Password");
        //  this.toastr.error(err.error, 'Error');
        this.toastr.error('Invalid Username or Password', 'Login Error!', {
          timeOut: 5000,
        }); 
       }
       
     }
     );
    }




    // if(this.loginForm.valid){
    //   this.ls.login(this.loginForm.value).subscribe((data: { data: any; })=>{
    //     this.ls.storeUserData(data.data);
    //     this.router.navigate(['/dashboard']);
    //   },(err: any)=>{
    //     console.log(err);

    //     // this.loginForm = this.fb.group(this.formLayout);

    //     this.toastr.error('Invalid Username or Password', 'Login Error!', {
    //       timeOut: 5000,
    //     }); 
    //   });
    // }  
  }
}
