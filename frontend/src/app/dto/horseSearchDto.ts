import { HorseSex } from "../types/horseSexEnum";
import { Owner } from "./owner";

export interface HorseSearchDto {
  name?: string;
  description?: string;
  bornAfter?: string;
  sex?: HorseSex;
  bornBefore?: string;
  owner?: number;
  limit?: number;
}
