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
    const contentLength = response.headers.valueOf('Content-Length');

    client.assert(contentLength === '0', 'Non-empty body');
};

export const assertContentType = contentType => assertEqual(
    contentType,
    response.contentType.mimeType,
    'Invalid content type'
);
