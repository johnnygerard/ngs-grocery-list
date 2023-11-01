export const assertEqual = (expected, actual, message) => client.assert(
    expected === actual,
    message + ` (expected: ${expected}, actual: ${actual})`
);
