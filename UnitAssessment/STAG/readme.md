
GameServer -> [Location, GameAction, Artefact, Furniture, Character, Player]

Player -> [GameEntity, Artefact, Location]

Location -> [Artefact, Furniture, Character, GameEntity]

GameClient -> []

GameAction -> []

Furniture -> [GameEntity]

Character -> [GameEntity]

Artefact -> [GameEntity]

GameEntity -> []

