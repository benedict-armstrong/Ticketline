export interface Sector {
  id: number;
  type: "seated" | "standing" | "stage";
  name: string;
  description: string;
  color: string; // Hex code
}
