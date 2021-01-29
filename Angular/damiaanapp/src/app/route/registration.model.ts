import { VirtualTimeScheduler } from 'rxjs';

interface RegistrationDTOJson {
  id: number;
  paid: boolean;
  tShirt: boolean;
  tshirtSize: string;
}

export class Registration {
  constructor(
    private _id: number,
    private _paid: boolean,
    private _shirt: boolean,
    private _size: string
  ) {}

  public get id(): number {
    return this._id;
  }
  public get paid(): boolean {
    return this._paid;
  }

  public get shirt(): boolean {
    return this._shirt;
  }

  public get size(): string {
    return this._size;
  }

  static fromJSON(json: RegistrationDTOJson): Registration {
    const registratrion = new Registration(
      json.id,
      json.paid,
      json.tShirt,
      json.tshirtSize
    );

    return registratrion;
  }

  toJSON(): RegistrationDTOJson {
    return {
      id: this._id,
      paid: this._paid,
      tShirt: this._shirt,
      tshirtSize: this._size,
    };
  }
}
