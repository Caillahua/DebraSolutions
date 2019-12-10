export const required = value => !!value;

export const alphaNumeric = value => /^[a-z0-9]*$/i.test(value);

export const numeric = value => !isNaN(Number(value));

export const alphaOnly = value => /^[a-zA-ZÀ-ÿ\u00f1\u00d1]+(\s*[a-zA-ZÀ-ÿ\u00f1\u00d1]*)*[a-zA-ZÀ-ÿ\u00f1\u00d1]+\s*$/g.test(value);

export const dni = value => value.length === 8 && numeric(value);

export const ruc = value => value.length === 11 && numeric(value);

export const ce = value => value.length >= 9 && alphaNumeric(value);

export const minLength = (value, length) => value.length >= length;

export const email = value => !!!value || /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}\s*$/i.test(value);

export const licensePlate = value => /^([A-Za-z0-9]{6,8})$/g.test(value);

export const phone = value => [7, 9].includes(value.length) && numeric(value);

export default {
  required,
  alphaNumeric,
  numeric,
  alphaOnly,
  dni,
  ruc,
  ce,
  minLength,
  email,
  licensePlate,
  phone,
};
