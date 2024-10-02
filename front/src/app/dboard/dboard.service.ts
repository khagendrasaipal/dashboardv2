import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AppConfig } from '../app.config';
import { ApiService } from '../api.service';

@Injectable({
  providedIn: 'root'
})
export class DboardService {
  constructor(private http: HttpClient,private api: ApiService) { }

  baseUrl: string = AppConfig.baseUrl;
  url= this.baseUrl+'dboard';
  // url = 'http://localhost:8010/category';

  create(data: any,type:any) {
    // console.log(data);
    return this.http.post(this.baseUrl+"dashboard/get-ind-data?orgid="+localStorage.getItem("orgid")+"&type="+type, data);

  }

  createOpen(data: any,type:any,df:any) {
    // console.log(data);
    return this.http.post(this.baseUrl+"dashboard/get-ind-data?orgid="+df+"&type="+type, data);

  }

  createCharts(myear:any,mindicator:any,charttype:any,type:any){
    let f:any={"mindicator":[],"myear":[]};
    const inds = mindicator.split(",");
      for(const i of inds){
        f.mindicator.push(i);
      }
      const yrs = myear.split(",");
      for(const j of yrs){
        f.myear.push(parseInt(j));
      }


    return this.http.post(this.baseUrl+"dashboard/get-ind-data?orgid="+localStorage.getItem("orgid")+"&charttype="+charttype+"&type="+type,f);
  }

  createfinanceCharts(myear:any,mindicator:any,charttype:any,type:any){
    return this.http.post(this.baseUrl+"dashboard/get-ind-data?orgid="+localStorage.getItem("orgid")+"&charttype="+charttype+"&type="+type+"&findicator="+mindicator+"&fyear="+myear,type);
  }
  saveConfig(data:any,myear:any,mindicator:any,charttype:any,cid:any){
    return this.http.post(this.baseUrl+"dashboard/saveConfig?orgid="+localStorage.getItem("orgid")+"&myear="+myear+"&mindicator="+mindicator+"&charttype="+charttype+"&cid="+cid, data);
  }
  update(id: any, data: any) {

    return this.http.put(this.url + '/' + id, data);
    // return this.api.update(this.path,id,data);
  }

  getList(ldate:any,perPage: string | number, page: string | number, searchTerm?: string, sortKey?: string, sortDir?: boolean) {

    let urlPart = '?perPage=' + perPage + '&page=' + page+'&ldate='+ldate;
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
    return this.http.delete(this.baseUrl + 'dashboard/deleteCharts/' + id);

  }
  getProgram(){
    return this.http.get(this.url + '/getProgram');
  }

  getConfig(cid:any){
    return this.http.get(this.baseUrl + 'dashboard/getConfig?orgid='+localStorage.getItem("orgid")+"&cid="+cid);
  }

  SaveSetting(id:any){
    return this.http.get(this.baseUrl + 'dashboard/SaveSetting?id='+id);
  }
  getDefault(){
    return this.http.get(this.url + '/getDefault');
  }
  getIndicators(pid:any){
    return this.http.get(this.baseUrl + 'dashboard/getIndicators');
  }
  getSlider(){
    return this.http.get(this.baseUrl + 'dashboard/getSlider');
  }

  getfy(){
    return this.http.get(this.baseUrl + 'dashboard/getFyConfig');
  }

  getHfCount(){
    const headers = new HttpHeaders();
    return this.http.get('https://nhfr.mohp.gov.np/health-registry/auth-count?roles=superuser',{headers});
  }

  getCovidCount(){
    return this.http.get('https://nhfr.mohp.gov.np/health-registry/covid-count?roles=superuser');
  }

  getDashboardData(iid:any,fy:any){
    return this.http.get(this.url + '/getData?iid='+iid+'&fy='+fy);
  }
  updateSelf(id:any,data:any){
    return this.http.put(this.baseUrl + 'user/change-pasword-self/' + id, data);
  }

  getDashboardCombineData(pid:any,tid:any){
    return this.http.get(this.baseUrl + 'getDashboardAdmin?orgid=0&pid='+pid+"&tid="+tid);
  }
  getComposite(){
    return this.http.get(this.url + '/getComposite');
  }
  editSetting(pid:any,value:any,tid:any){
    return this.http.get(this.baseUrl + 'setSetting?orgid=0&pid='+pid+"&tid="+tid+"&value="+value);
  }
  getSectorData(req:any){
    return this.http.get(this.baseUrl+"sutra?repType="+req+"&fy=2078&orgid=0")
  }

  gettodayLog(usertype:any,dates:any){
    return this.http.get(this.baseUrl + 'dashboard/getTodayLogs?usertype='+usertype+'&dates='+dates);
  }
  getorganization(){
    return this.http.get(this.baseUrl + 'organization/getorganization');
}

getFyrange(){
  return this.http.get(this.baseUrl + 'organization/getFyrange');
}
}
