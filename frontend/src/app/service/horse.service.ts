import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";

import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { Horse } from "../dto/horse";
import { HorseSearchDto } from "../dto/horseSearchDto";

const baseUri = environment.backendUrl + "/horses";

@Injectable({
  providedIn: "root",
})
export class HorseService {
  httpOptions = {
    headers: new HttpHeaders({ "Content-Type": "application/json" }),
  };

  constructor(private http: HttpClient) {}

  /**
   * Get all horses stored in the system
   *
   * @return observable list of found horses.
   */
  getAllHorses(): Observable<Horse[]> {
    return this.http.get<Horse[]>(baseUri);
  }

  getHorse(id: number): Observable<Horse> {
    return this.http.get<Horse>(baseUri + `/${id}`);
  }

  getHorseFamilytree(id: number, generations: number): Observable<Horse> {
    console.log(baseUri + `/familytree/${id}?d=${generations}`);
    return this.http.get<Horse>(baseUri + `/familytree/${id}?d=${generations}`);
  }

  updateHorse(horse: Horse): Observable<Horse> {
    return this.http.put<Horse>(baseUri, horse, this.httpOptions);
  }

  addHorse(horse: Horse): Observable<Horse> {
    return this.http.post<Horse>(baseUri, horse, this.httpOptions);
  }

  deleteHorse(id: number): Observable<Horse> {
    return this.http.delete<Horse>(baseUri + `/${id}`);
  }
  search(searchParameters: HorseSearchDto): Observable<Horse[]> {

    const searchParametersStandardized = JSON.parse(JSON.stringify(searchParameters));
    const urlSearchParams = new URLSearchParams(searchParametersStandardized).toString();
    
    return this.http.get<Horse[]>(
      baseUri + "/selection?" + urlSearchParams
    );
  }
}
