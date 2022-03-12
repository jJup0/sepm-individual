import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";

import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { Horse } from "../dto/horse";

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

  addHorse(horse: Horse): Observable<Horse> {
    console.log("horse service about to post horse:" + JSON.stringify(horse));
    return this.http.post<Horse>(baseUri, horse, this.httpOptions);
  }
}
