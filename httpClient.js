// This file is imported by the HTTP Client.
export const assertEqual = (expected, actual, message) => client.assert(
    expected === actual,
    `${message} (expected: ${expected}, actual: ${actual})`
);

export const assertStatus = statusCode => assertEqual(
    statusCode,
    response.status,
    'Invalid status code'
);

export const assertEmptyBody = () => {
    client.assert(response.body === null, 'Non-empty body');
};

export const assertContentType = contentType => assertEqual(
    contentType,
    response.contentType.mimeType,
    'Invalid content type'
);

export const assertJsonBody = expected => {
    const actual = response.body;
    const message = `JSON values are not equal:
Expected: ${expected}
Actual: ${actual}`;

    assertContentType('application/json');
    client.assert(isDeepEqual(expected, actual), message);
};

export const assertNotNull = (value, message) => {
    client.assert(value !== null, message);
};

const isDeepEqual = (expected, actual) => {
    if (expected === actual) return true;
    if (typeof expected !== 'object' || typeof actual !== 'object')
        return false;

    const expectedKeys = Object.keys(expected);
    const actualKeys = Object.keys(actual);

    if (expectedKeys.length !== actualKeys.length) return false;

    for (let key of expectedKeys) {
        if (!actualKeys.includes(key)) return false;
        if (!isDeepEqual(expected[key], actual[key])) return false;
    }

    return true;
};
