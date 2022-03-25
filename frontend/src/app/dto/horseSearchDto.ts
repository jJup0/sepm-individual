import { HorseSex } from "./horseSexEnum";
import { Owner } from "./owner";

export interface HorseSearchDto {
  name?: string;
  description?: string;
  bornAfter?: string;
  sex?: HorseSex;
  bornBefore?: string;
  owner?: Owner;
  limit?: number;
}
