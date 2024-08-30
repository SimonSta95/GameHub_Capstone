export type User = {
    id: string,
    username: string,
    gitHubId: string,
    avatarUrl: string,
    role: string,
    gameLibrary: string[],
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

export type GameLibraryOptions = {
    userId: string
    gameId: string
}