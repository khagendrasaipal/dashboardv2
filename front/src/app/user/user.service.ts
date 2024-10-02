import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AppConfig } from '../app.config';



@Injectable({
    providedIn: 'root' // just before your class
  })



export class UserService {
 
 
    baseUrl: string = AppConfig.baseUrl;
    url= this.baseUrl+'user';
    
constructor(private http: HttpClient) {}

   
    create(data: any) {
        // console.log(data);
        return this.http.post(this.url,data);
        
    }
    update(id: any, data: any) {
        return this.http.put(this.url + '/' + id, data);
        // return this.api.update(this.path,id,data);
    }

    getList(perPage: string | number, page: string | number, searchTerm?: string, sortKey?: string, sortDir?: boolean) {

        let urlPart = '?perPage=' + perPage + '&page=' + page;
        if (typeof searchTerm !== 'undefined' || searchTerm !== '') {
            urlPart += '&searchOption=all&searchTerm=' + searchTerm;
        }
        if (typeof sortKey !== 'undefined' || sortKey !== '') {
            urlPart += '&sortKey=' + sortKey;
        }
        if (typeof sortDir !== 'undefined' && sortKey !== '') {
            if (sortDir) {
                urlPart += '&sortDir=desc';
            } else {
                urlPart += '&sortDir=asc';
            }
        }
        return this.http.get(this.url + urlPart);
        
    }



    getEdit(id: string) {
        
        return this.http.get(this.url + '/' + id);
        
    }
    remove(id: string) {
        
        return this.http.delete(this.url + '/' + id);
        
    }

    // getAdminLevel() {
    //     return this.http.get(this.url + '/get-adminlvl');
    // }
    // getFederal() {
    //     return this.http.get(this.url + '/get-federal');
    // }
    // getParentFilter(adminid:any,levelid:any) {
    //     return this.http.get(this.url + '/get-parent-orgs?adminid='+adminid+'&levelid='+levelid);
    // }
    // getParent(aid:any) {
    //     return this.http.get(this.url + '/get-parent/'+aid);
    // }
    // getOrglevel() {
    //     return this.http.get(this.url + '/get-orglevel');
    // }

    // getProvinces() {
    //     return this.http.get(this.baseUrl + 'district/get-province');
    // }

    // getDistricts(provinceid: any) {
    //     return this.http.get(this.baseUrl + 'district/get-districts/'+provinceid);
    //   }

    //   getPalikas(districtid: any) {
    //     return this.http.get(this.baseUrl + 'district/get-palikas/'+districtid);
    //   }

    //   getAdminStr(adminlevel: any) {
    //     return this.http.get(this.baseUrl + 'district/get-adminstr/'+adminlevel);
    //   }
    //   getAdmins(lid:any){
    //     return this.http.get(this.url + '/get-admin/'+lid);
    //   }

      getOrgs() {
        return this.http.get(this.url + '/get-orgs');
    }
   
}
