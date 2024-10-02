import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { ApiService } from "../api.service";
import { AppConfig } from "../app.config";

@Injectable()
export class LoginService {
    
    url = "auth/login";
    usrKey = "OASUSR";
    altToken = "token_bk";
    altUser = "user_bk";
    baseUrl: string = AppConfig.baseUrl;
    
    constructor(private api: ApiService, private router: Router,private http: HttpClient) { }

    login(data: any) {
        return this.api.post('sign-in', data);
    }

    loginData(data:any){
        
        return this.api.post('sign-in-admin', data);
    }

    storeUserData(data: any) {
        // console.log(data.user.orgid);
        localStorage.setItem(this.usrKey, JSON.stringify(data));
        localStorage.setItem("orgid",data.user.orgid);
        localStorage.setItem("usertype",data.user.usertype);
    }

    retriveUserData() {
        const userInfo = localStorage.getItem(this.usrKey);
        if (userInfo != null) {
            try {
                // console.log(JSON.parse(userInfo));
                // localStorage.setItem("orgid",JSON.parse(userInfo).user.orgid);
                return JSON.parse(userInfo);
            } catch (e) {
                return null;
            }

        }
        return null;
    }

    removeUserData() {
        
            localStorage.removeItem(this.usrKey);
            localStorage.removeItem("orgid");
            return;
        
    }

    // loginWithAnotherUser(userId:string){
    //     return new Promise((resolve,reject)=>{
    //         const userData = this.retriveUserData();
    //         if(userData.hasOwnProperty(this.altUser) || userData.hasOwnProperty(this.altToken)){
    //             return reject(false);
    //         }
    //         this.api.post('get-user-token',{"userid":userId}).subscribe(data=>{
    //             if(data.hasOwnProperty('token') && data?.token!=''){
    //                 if(userData!=null){
    //                     const cuser = userData.user;
    //                     const ctoken = userData.token;
    //                     data[this.altUser] = cuser;
    //                     data[this.altToken] = ctoken;
    //                     this.storeUserData(data);
    //                     resolve(true);
    //                 }
    //             }
    //             reject(false);
    //         },err=>{
    //             reject(err);
    //         });
    //     });
    // }

    // restoreUserLogin(){
    //     const userData = this.retriveUserData();
    //     if(userData!=null){
    //         if(userData.hasOwnProperty(this.altUser) && userData.hasOwnProperty(this.altToken)){
    //             if( userData[this.altUser]!='' && userData[this.altToken]!=''){
    //                 const bkUser = userData[this.altUser];
    //                 const bkToken = userData[this.altToken];
    //                 this.storeUserData({"user":bkUser,"token":bkToken});
    //                 return true;
    //             }
    //         }
    //     }
    //     return false;
    // }
    getuserinfo(){
        return this.http.get(this.baseUrl + 'organization/getorgs/session');
    }

    getorganization(){
        return this.http.get(this.baseUrl + 'organization/getorganization');
    }

    getorganizationList() {
        return this.http.get(this.baseUrl + 'organization/getorgs');
      }
}
