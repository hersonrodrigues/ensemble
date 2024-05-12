const OMDB_API_KEY = "ed17342a";

/**
 * Api service to list all movies from https://www.omdbapi.com
 * the @param search can be used to search an specific movie.
 * Example of use of this api: Get to http://API-URL/api/movies?search=Spiderman
 */
export async function GET(request) {
    // I case it has the param search we store it
    const search = request.nextUrl.searchParams.get("search");

    let apiUrl = `https://www.omdbapi.com/?apikey=${OMDB_API_KEY}&s=Spiderman`;

    // @see https://www.omdbapi.com/
    if (search != null) {
        apiUrl = `https://www.omdbapi.com/?apikey=${OMDB_API_KEY}&s=${search}`;
    }

    const responsePrepare = await fetch(apiUrl);
    const response = await responsePrepare.json();

    const isSuccess = responsePrepare.Response == "False" || true;

    let error = null;
    if (response.Error != null) {
        error = response.Error;
    }

    // Store the results of the Movie Api response
    let result = [];
    if (isSuccess) {
        // The api use the Search container, so we want to have a new pattern here
        result = response.Search
    }

    // Return 200 with the data
    return Response.json({ success : isSuccess, error, result }, { status: 200 });
}