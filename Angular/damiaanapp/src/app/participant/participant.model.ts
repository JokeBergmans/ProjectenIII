interface ParticipantDTOJson {
    id: number;
    name: string;
    firstName: string;
    email: string;
    birthDate: Date; 
    street: string;
    number: number;
    city: string;
    postalCode: number;
    canBeFollowed: boolean;
}

export class Participant {
    constructor(
        private _id : number,
        private _name : string,
        private _firstName : string,
        private _email : string,
        private _birthDate : Date,
        private _street: string,
        private _number: number,
        private _city: string,
        private _postalCode: number,
        private _canBeFollowed: boolean
    ) {}

    public get id(): number {
        return this._id;
    }
    public get name(): string {
        return this._name;
    }
    public get firstName(): string {
        return this._firstName;
    }
    public get email(): string {
        return this._email
    }
    public get birthDate(): Date {
        return this._birthDate;
    }
    public get street(): string {
        return this._street;
    }
    public get number(): number {
        return this._number;
    }
    public get city(): string {
        return this._city;
    }
    public get postalCode(): number {
        return this._postalCode;
    }
    public get canBeFollowed(): boolean {
        return this._canBeFollowed;
    }

    static fromJSON(json: ParticipantDTOJson): Participant {
        const participant = new Participant(
          json.id,
          json.name,
          json.firstName,
          json.email,
          json.birthDate,
          json.street,
          json.number,
          json.city,
          json.postalCode,
          json.canBeFollowed
        );

        return participant;
    }
    
    toJSON(): ParticipantDTOJson {
        return {
          id: this._id,
          name: this._name,
          firstName: this._firstName,
          email: this._email,
          birthDate: this._birthDate,
          street: this._street,
          number: this._number,
          city: this._city,
          postalCode: this._postalCode,
          canBeFollowed: this._canBeFollowed
        };
    }
}