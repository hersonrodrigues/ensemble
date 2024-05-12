const EXTERNAL_FIREBASE = "https://pawsplace-72a2a-default-rtdb.firebaseio.com/movies-temp"

/**
 * 
 * Api service to define movie liked, the user needs to pass the deviceId 
 * and the imdbID in order to like that movie.
 * Example of use of this api: Post to http://API-URL/api/movies 
 * with body json { deviceId:'123', imdbID:'321' }
 */
export async function POST(request) {
  const body = await request.json();

  // Check if deviceId, imdbID, and flag are provided
  if (!body.deviceId || !body.imdbID || body.flag === undefined) {
      return new Response(
          JSON.stringify({ success: false, error: 'Missing body json params: deviceId (string), imdbID (string), and flag (boolean)' }),
          { status: 400, headers: { 'Content-Type': 'application/json' } }
      );
  }

  // Construct the URL for the external Firebase endpoint
  const url = `${EXTERNAL_FIREBASE}/${body.deviceId}/${body.imdbID}.json`;

  try {
      // Update the flag in Firebase using the PUT method
      const response = await fetch(url, {
          method: 'PUT',
          headers: {
              'Content-Type': 'application/json',
          },
          body: JSON.stringify({ flag: body.flag }),
      });

      if (!response.ok) {
          throw new Error('Failed to update flag');
      }

      // Return success response
      return new Response(
          JSON.stringify({ success: true, result: body }),
          { status: 200, headers: { 'Content-Type': 'application/json' } }
      );
  } catch (error) {
      return new Response(
          JSON.stringify({ success: false, error: error.message }),
          { status: 500, headers: { 'Content-Type': 'application/json' } }
      );
  }
}

/**
 * Api service to list all movies liked by deviceId
 * the @param deviceId has to be passed to list all movies flaged
 * Example of use of this api: Get to http://API-URL/api/movies/like
 */
export async function GET(request) {
  // I case it has the param search we store it
  const deviceId = request.nextUrl.searchParams.get("deviceId");

  // Check if deviceId and movieId are provided
  if (!deviceId) {
    return Response.json({ success : false, error: 'Param deviceId is required' }, { status: 400 });
  }

  try {
    // Fetch Firebase using the GET method on specific deviceID node
    const response = await fetch(`${EXTERNAL_FIREBASE}/${deviceId}.json`);

    // Store the results of the Movie Api response
    const movies = await response.json();

    // Filter the movies where the flag is true and extract their IDs
    // This process is to make easy on font-end to consult a movie id into a array list
    let flaggedMovieIds = [];
    if (movies) {
      flaggedMovieIds = Object.entries(movies)
      .filter(([movieId, movie]) => movie.flag === true)
      .map(([movieId]) => movieId);
    } 

    // Return 200 with the data
    return Response.json({ success : true, result: flaggedMovieIds }, { status: 200 });
  } catch (error) {
    // Return an error response if an exception occurs
    return new Response(
        JSON.stringify({ success: false, error: error.message }),
        { status: 500, headers: { 'Content-Type': 'application/json' } }
    );
  }
}