import { Container } from '../model/container.model';
 
export class TemplateContainerWalker {

  containers: Container[];

  constructor(containers: Container[]) {
    this.containers = containers;
  }

  walk(visitor: (container: Container) => void) {
    this.walkAndProcess(this.containers, visitor);
  }
  
  private walkAndProcess(containers: Container[], visitor: (container: Container) => void) {
    
    containers.forEach(container => {
      
      // visit this container
      visitor(container);

      // visit child containers
      this.walkAndProcess(container.container, visitor);
    });
    
  }
  
}