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
    const shortHandMapping = {
      name: "n",
      description: "d",
      bornAfter: "ba",
      bornBefore: "bb",
      sex: "s",
      owner: "o",
      limit: "l",
    };
    const searchParamsArr: string[] = [];
    for (const paramName in searchParameters) {
      if (searchParameters[paramName] !== null) {
        let param = searchParameters[paramName];
        if (paramName === "bb" || paramName === "ba") {
          param = searchParameters[paramName].toISOString().slice(0, 10);
        }
        // TODO!!! OWNER AS DATA STRUCTURE
        searchParamsArr.push(`${shortHandMapping[paramName]}=${param}`);
      }
    }
    console.log(
      "searching for: " + baseUri + "/search?" + searchParamsArr.join("&")
    );
    return this.http.get<Horse[]>(
      baseUri + "/search?" + searchParamsArr.join("&")
    );
  }
}
