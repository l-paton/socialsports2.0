export class Requirement{

    constructor(minAge: number, maxAge: number, gender: string, reputation: number){
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.gender = gender;
        this.reputation = reputation;
    }

    minAge: number;
    maxAge: number;
    gender: string;
    reputation: number;
}