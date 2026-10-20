import dotenv from 'dotenv';

dotenv.config();

export class S1ClientService {
  private s1Url = process.env.S1_SERVICE_URL || 'http://localhost:3001/api/agreements';

  public async verifyPlazaAvailability(idPlaza: number): Promise<boolean> {
    try {
      const response = await fetch(this.s1Url);
      const result = await response.json();
      if (!result.success) return false;

      const plaza = result.data.find((item: any) => Number(item.id_plaza) === Number(idPlaza));
      if (!plaza) return false;

      return plaza.estado_convenio === 'VIGENTE' && (plaza.cupos_ofrecidos - plaza.cupos_ocupados) > 0;
    } catch (error) {
      console.error('Error al verificar disponibilidad con Microservicio S1:', error);
      return false;
    }
  }
}
