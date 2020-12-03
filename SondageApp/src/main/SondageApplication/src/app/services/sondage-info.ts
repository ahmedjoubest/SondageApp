import {DateInfo} from './date-info';
import {LieuInfo} from './lieu-info';

export class SondageInfo {
    id : number;
    titre: string;
    description: string;
    dates: string[];
    lieux: string[];

    constructor(id:number, titre: string, description: string, lieux: string[] = [], dates: string[] = []) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.lieux = lieux;
        this.dates = dates;
    }
}
