export type User = {
    id: string,
    username: string,
    gitHubId: string,
    role: string,
    gameLibrary: Game[],
}

export type Game = {
    id: string,
    title: string,
    genre: string,
    releaseDate: Date,
    platforms: string[],
    developer: string,
    publisher: string,
    description: string,
    coverImage: string
}