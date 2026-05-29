import os
import json

biome_dir = 'geoworld-datapack/data/geoworld/worldgen/biome/'

for root, dirs, files in os.walk(biome_dir):
    for file in files:
        if file.endswith('.json'):
            filepath = os.path.join(root, file)
            with open(filepath, 'r') as f:
                data = json.load(f)

            modified = False
            if 'carvers' in data and data['carvers'] == {}:
                data['carvers'] = []
                modified = True

            # Ensure spawn_costs is an empty object if it exists but is empty array or something else,
            # though based on previous check it's mostly {} already.
            if 'spawn_costs' not in data:
                data['spawn_costs'] = {}
                modified = True
            elif data['spawn_costs'] == []:
                data['spawn_costs'] = {}
                modified = True

            if modified:
                with open(filepath, 'w') as f:
                    json.dump(data, f, indent=2)
                print(f"Fixed {filepath}")
