package de.objectcode.time4u.server.entities.revision;

/**
 * Revision number generator.
 * 
 * @author junglas
 */
public interface IRevisionGenerator
{
  long getNextRevision(EntityType entityType, long part);
}
