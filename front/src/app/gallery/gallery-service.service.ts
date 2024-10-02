import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AppConfig } from '../app.config';


@Injectable({
  providedIn: 'root'
})
export class GalleryServiceService {

  constructor(private http: HttpClient) { }

  baseUrl: string = AppConfig.baseUrl;
  url= this.baseUrl+'infographics';

  getSlider(){
    return this.http.get(this.baseUrl + 'dashboard/getSliders');
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
}
