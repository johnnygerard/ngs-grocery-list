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

export const assertEmptyBody = () => client.assert(
    '0' === response.headers.valueOf('Content-Length'),
    'Non-empty body'
);

export const assertContentType = contentType => assertEqual(
    contentType,
    response.contentType.mimeType,
    'Invalid content type'
);
